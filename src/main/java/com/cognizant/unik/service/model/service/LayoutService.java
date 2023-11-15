package com.cognizant.unik.service.model.service;

import java.io.File;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognizant.unik.data.jdbc.dao.LayoutDAO;
import com.cognizant.unik.data.jdbc.dao.LayoutRepository;
import com.cognizant.unik.data.model.CodeGenRequest;
import com.cognizant.unik.data.model.LayoutEntity;
import com.cognizant.unik.service.model.data.Response;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


@Controller
@RequestMapping("/rest/layout")
public class LayoutService {

	@Autowired
	private LayoutRepository layoutRepository;
	
	@Autowired
	private LayoutDAO layoutDao;
	
	@Value("${app.env}")
	private String appEnv;

	@Value("${cloud.code.gen.path}")
	private String cloudCodeGenPath;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public void saveLayout(@RequestParam("key") String key,
			@RequestBody String jsonLayoyt) {
		JSONObject object;
		try {
			object = new JSONObject(jsonLayoyt);
			String xml = XML.toString(object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		layoutDao.saveLayout(key, jsonLayoyt);
	}

	
	public void transformFormLayout(String moduleName,String layoutXml) {
		 try {
			 System.out.println(layoutXml);
			 TransformerFactory factory = TransformerFactory.newInstance();
			 Source xslt = null;
			 if(moduleName.toUpperCase().indexOf("STARTFORM") >=0) {
				  xslt = new StreamSource(this.getClass().getClassLoader().getResourceAsStream("startformlayout.xsl"));
			 }
			 else if (moduleName.toUpperCase().indexOf("POPUP") >=0) {
				 xslt = new StreamSource(this.getClass().getClassLoader().getResourceAsStream("popupmodal.xsl"));
			 }
			 else {
				 xslt = new StreamSource(this.getClass().getClassLoader().getResourceAsStream("formlayout.xsl"));
			 }
				
				    String filename = "d:\\fragments\\"+moduleName+".html";
				    Transformer transformer = factory.newTransformer(xslt );

				    Source text = new StreamSource(new StringReader(layoutXml));
				    transformer.transform(text, new StreamResult(new File(filename)));
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	@RequestMapping(value = "/callAngCodeGenTool", method = RequestMethod.POST)
	@ResponseBody
	public Response callAngularTool(@RequestBody String inputParams) {
		System.out.println("Entry -> LayoutService -> callAngularTool");
		Gson gson = new Gson();
		
		CodeGenRequest codeGenRequest=gson.fromJson(inputParams, CodeGenRequest.class);		
		LayoutEntity layoutEntity = layoutRepository.findById(new BigInteger(codeGenRequest.getScreenName())).get();
		codeGenRequest.setAppName("uimodeller");
		codeGenRequest.setCss("component.css");
		codeGenRequest.setScreenName(layoutEntity.getLayoutName());
		codeGenRequest.setUiLayout(layoutEntity.getLayout());
		codeGenRequest.setUiName(codeGenRequest.getScreenName());
		codeGenRequest.setSpringBootConfigName("springboot-default");
		
		if(StringUtils.isEmpty(codeGenRequest.getGenLoginScreen())) {
			codeGenRequest.setGenLoginScreen("true");
		}
		
		List<CodeGenRequest> reUsableComponents = getAllReusableComponents(codeGenRequest, layoutEntity);
		if (reUsableComponents != null && reUsableComponents.isEmpty() == false) {
			for (CodeGenRequest request : reUsableComponents) {
				layoutDao.invokeCodeGenerator(request);
			}
		}
		
		String codeGenResponse = layoutDao.invokeCodeGenerator(codeGenRequest);
		
		Response response = new Response();
		response.setMessage(codeGenResponse);
		
		if ("cloud".equalsIgnoreCase(appEnv)) {
			response.setPath(cloudCodeGenPath + "/" + codeGenRequest.getProjectName() + getZipExtension());
		} else {
			response.setPath(codeGenRequest.getCodeGenPath() + "/" + codeGenRequest.getProjectName() + getZipExtension());
		}
		
		System.out.println("Exit -> LayoutService -> callAngularTool");
		return response;
	}
	
	private List<CodeGenRequest> getAllReusableComponents(CodeGenRequest codeGenRequest, LayoutEntity layoutEntity) {
		List<CodeGenRequest> compLists = new ArrayList<CodeGenRequest>();
		
		JsonObject layout = new Gson().fromJson(layoutEntity.getLayout(), JsonObject.class);
		
		getAllComponents(compLists, layout.get("layout"), codeGenRequest);
		
		return compLists;
	}


	private void getAllComponents(List<CodeGenRequest> compLists, JsonElement jsonElement, CodeGenRequest codeGenRequest) {
		if (jsonElement.isJsonArray()) {
			JsonArray array = jsonElement.getAsJsonArray();
			for (JsonElement arrayElement : array) {
				getAllComponents(compLists, arrayElement, codeGenRequest);
			}
		} else {
			JsonObject element = jsonElement.getAsJsonObject();
			if (element.has("columns")) {
				JsonArray array = element.get("columns").getAsJsonArray();
				for (JsonElement arrayElement : array) {
					getAllComponents(compLists, arrayElement, codeGenRequest);
				}	
			}
			
			String type = element.get("type").getAsString();
			if ("component".equalsIgnoreCase(type)) {
				CodeGenRequest request = new CodeGenRequest();
				BeanUtils.copyProperties(codeGenRequest, request);

				List<LayoutEntity> entities = layoutRepository.findByLayoutName(element.get("componentName").getAsString());
				
				request.setUiLayout(entities.get(0).getLayout());
				request.setComponentName("appcommon");
				request.setModuleName("appcommon");
				request.setReusableComponent("True");
				request.setParams(entities.get(0).getParams());
				
				compLists.add(request);
			}
		}
	}

	private String getZipExtension() {
    	boolean isWindows = System.getProperty("os.name")
				  .toLowerCase().startsWith("windows");
    	
    	if (isWindows) {
    		return ".zip";
    	}
    	
    	return ".tar.gz";
    }
	
	@RequestMapping(value = "/env", method = RequestMethod.GET)
	@ResponseBody
	public String getAppEnvironment() {
		return appEnv;
	}
}
