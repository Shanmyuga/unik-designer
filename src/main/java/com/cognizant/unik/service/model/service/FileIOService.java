/**
 * 
 */
package com.cognizant.unik.service.model.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 238209
 *
 */
@Controller
public class FileIOService {

	@Autowired
	private ServletContext servletContext;

	@RequestMapping("/download")
	public ResponseEntity<InputStreamResource> downloadFile(
			@RequestBody String fileName) throws IOException {

		MediaType mediaType = getMediaTypeForFileName(this.servletContext, fileName);
		System.out.println("fileName: " + fileName);
		System.out.println("mediaType: " + mediaType);

		File file = new File(fileName);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		return ResponseEntity.ok()
				// Content-Disposition
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
				// Content-Type
				.contentType(mediaType)
				// Contet-Length
				.contentLength(file.length()) //
				.body(resource);
	}
	
	@RequestMapping("/download1")
	public ResponseEntity<Resource> downloadFile1(
			@RequestBody String fileName) throws IOException {
		fileName = fileName.replace("\\", "/");

		MediaType mediaType = getMediaTypeForFileName(this.servletContext, fileName);
		System.out.println("fileName1: " + fileName);
		System.out.println("mediaType1: " + mediaType);
		System.out.println("overriding mediaType to: application/zip");

		File file = new File(fileName);
		Path path = Paths.get(file.getAbsolutePath());
		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

		return ResponseEntity.ok()
				// Content-Disposition
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
				.header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
				// Content-Type
				.contentType(mediaType)
				// Contet-Length
				.contentLength(file.length()) //
				.body(resource);
	}
	
	@RequestMapping("/download2")
	public void downloadFile2(HttpServletResponse response, 
			@RequestBody String fileName) throws IOException {

		fileName = fileName.replace("\\", "/");
		
		MediaType mediaType = getMediaTypeForFileName(this.servletContext, fileName);
		System.out.println("fileName2: " + fileName);
		System.out.println("mediaType2: " + mediaType.toString());

		File file = new File(fileName);

		response.setContentType(mediaType.toString());
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
        response.setContentLength((int) file.length());
        
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

	public MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
		// application/pdf
		// application/xml
		// image/gif, ...
		String mineType = servletContext.getMimeType(fileName);
		try {
			MediaType mediaType = MediaType.parseMediaType(mineType);
			return mediaType;
		} catch (Exception e) {
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}
}
