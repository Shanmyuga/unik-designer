package com.cognizant.unik.service.model.service;

import java.io.File;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognizant.unik.data.jdbc.dao.ComponentRepository;
import com.cognizant.unik.data.jdbc.dao.ConfigRepository;
import com.cognizant.unik.data.jdbc.dao.LayoutRepository;
import com.cognizant.unik.data.jdbc.dao.ResourceRepository;
import com.cognizant.unik.data.model.CodeGenRequest;
import com.cognizant.unik.data.model.ComponentConfig;
import com.cognizant.unik.data.model.ComponentEntity;
import com.cognizant.unik.data.model.ComponentParam;
import com.cognizant.unik.data.model.ComponentProperty;
import com.cognizant.unik.data.model.ConfigEntity;
import com.cognizant.unik.data.model.FormTemplate;
import com.cognizant.unik.data.model.LabelValueBean;
import com.cognizant.unik.data.model.LayoutEntity;
import com.cognizant.unik.data.model.Params;
import com.cognizant.unik.data.model.ResourceEntity;
import com.cognizant.unik.data.model.ResourceTemplate;
import com.cognizant.unik.service.model.data.Response;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


@Controller
@RequestMapping("/rest/design")
public class DesignerService {

	@Autowired
	private LayoutRepository layoutRepository;
	
	@Autowired
	private ConfigRepository configRepository;
	
	@Autowired
	private ComponentRepository componentRepository;
	
	@Autowired
	private ResourceRepository resourceRepository;
	
	@Value("${app.env}")
	private String appEnv;

	@Value("${cloud.code.gen.path}")
	private String cloudCodeGenPath;
	
	@RequestMapping(value = "/saveConfig", method = RequestMethod.POST)
	@ResponseBody
	public void saveConfig(@RequestBody String jsonLayout) {
		
		JsonObject json = new Gson().fromJson(jsonLayout, JsonObject.class);
		
		String uiLang = json.get("uiLang").getAsString();
		List<ConfigEntity> defConfigList = 
				configRepository.findByConfigLanguageAndDefaultConfig(uiLang, true);
		
		ConfigEntity entity = new ConfigEntity();
		entity.setConfigName(json.get("configName").getAsString());
		entity.setConfigLanguage(uiLang);
		entity.setDefaultConfig(false);
		
		if (defConfigList != null && defConfigList.isEmpty() == false) {
			entity.setDefaultConfigId(defConfigList.get(0).getId());
			entity.setResourceTemplates(defConfigList.get(0).getResourceTemplates());
		}
		//entity.setFormTemplates(formTemplates);
		
		configRepository.save(entity);
	}
	
	@RequestMapping(value = "/saveComponent", method = RequestMethod.POST)
	@ResponseBody
	public void saveComponent(@RequestBody String jsonLayout) {
		
		JsonObject json = new Gson().fromJson(jsonLayout, JsonObject.class);
		
		ComponentEntity entity = null;
		if (json.get("componentId") != null) {
			entity = componentRepository.findById(new BigInteger(json.get("componentId").getAsString())).get();
		}
		
		if (entity == null) {
			entity = new ComponentEntity();
		}
		
		if (json.get("baseComponentName") != null) {
			entity.setComponentName(json.get("baseComponentName").getAsString());
		}
		
		if (json.get("displayName") != null) {
			entity.setDisplayName(json.get("displayName").getAsString());
		}
		
		if (json.get("containerElement") != null) {
			entity.setContainerElement(json.get("containerElement").getAsBoolean());
		}
		
		if (json.get("hideElement") != null) {
			entity.setHideElement(json.get("hideElement").getAsBoolean());
		}
		
		if (json.get("formDisplayTemplate") != null) {
			entity.setFormDisplayTemplate(json.get("formDisplayTemplate").getAsString());
		}
		
		if (json.get("rowCount") != null) {
			entity.setContainerRowCount(json.get("rowCount").getAsInt());
		} else if (entity.isContainerElement()) {
			entity.setContainerRowCount(1);
		}
		
		if (json.get("colCount") != null) {
			entity.setContainerColCount(json.get("colCount").getAsInt());
		} else if (entity.isContainerElement()) {
			entity.setContainerColCount(1);
		}
		
		if (json.get("selectedParams") != null) {
			List<ComponentParam> paramsList = new ArrayList<>();
			
			ComponentParam param = null;
			JsonArray paramsArray = json.get("selectedParams").getAsJsonArray();
			for (JsonElement element : paramsArray) {
				param = new ComponentParam();
				if (element.getAsJsonObject().get("paramName") != null) {
					param.setParamName(element.getAsJsonObject().get("paramName").getAsString());
				}
				
				if (element.getAsJsonObject().get("paramType") != null) {
					param.setParamType(element.getAsJsonObject().get("paramType").getAsString());
				}
				
				if (element.getAsJsonObject().get("defaultValue") != null) {
					param.setDefaultValue(element.getAsJsonObject().get("defaultValue").getAsString());
				}
				
				paramsList.add(param);
			}
			
			entity.setComponentParams(paramsList);
		}
		
		if (json.get("selectedProperties") != null) {
			List<ComponentProperty> propsList = new ArrayList<>();
			
			ComponentProperty prop = null;
			JsonArray paramsArray = json.get("selectedProperties").getAsJsonArray();
			for (JsonElement element : paramsArray) {
				prop = new ComponentProperty();
				if (element.getAsJsonObject().get("propertyName") != null) {
					prop.setPropertyName(element.getAsJsonObject().get("propertyName").getAsString());
				}
				
				if (element.getAsJsonObject().get("propertyKey") != null) {
					prop.setPropertyKey(element.getAsJsonObject().get("propertyKey").getAsString());
				}
				
				if (element.getAsJsonObject().get("propertyDataType") != null) {
					prop.setPropertyDataType(element.getAsJsonObject().get("propertyDataType").getAsString());
				}
				
				if (element.getAsJsonObject().get("options") != null) {
					if (StringUtils.equalsIgnoreCase(prop.getPropertyDataType(), "dropdown")) {
						JsonArray optionsArray = element.getAsJsonObject().get("options").getAsJsonArray();

						if (optionsArray != null && optionsArray.size() > 0) {
							LabelValueBean option = null;
							for (JsonElement optionEle : optionsArray) {
								option = new LabelValueBean();
								option.setDisplay(optionEle.getAsJsonObject().get("display").getAsString());
								option.setValue(optionEle.getAsJsonObject().get("value").getAsString());

								prop.getOptions().add(option);
							}
						}
					} else {
						prop.setOptions(null);
					}
				}
				
				propsList.add(prop);
			}
			
			entity.setComponentProps(propsList);
		}
		
		componentRepository.save(entity);
	}
	
	@RequestMapping(value = "/deleteComponent", method = RequestMethod.POST)
	@ResponseBody
	public void deleteComponent(@RequestBody String jsonLayout) {
		
		JsonObject json = new Gson().fromJson(jsonLayout, JsonObject.class);
		
		ComponentEntity entity = null;
		if (json.get("componentId") != null) {
			entity = componentRepository.findById(new BigInteger(json.get("componentId").getAsString())).get();
		}
		
		if (entity != null) {
			componentRepository.delete(entity);
		}
	}
	
	@RequestMapping(value = "/deleteConfig", method = RequestMethod.POST)
	@ResponseBody
	public void deleteConfig(@RequestBody String jsonLayout) {
		
		JsonObject json = new Gson().fromJson(jsonLayout, JsonObject.class);
		
		String configId = json.get("configId").getAsString();
		ConfigEntity defConfigList = 
				configRepository.findById(new BigInteger(configId)).get();
		
		if (defConfigList != null) {
			configRepository.delete(defConfigList);
		}
	}
	
	@RequestMapping(value = "/saveForm", method = RequestMethod.POST)
	@ResponseBody
	public void saveForm(@RequestBody String jsonLayout) {
		
		JsonObject json = new Gson().fromJson(jsonLayout, JsonObject.class);
		
		String configId = json.get("configId").getAsString();
		ConfigEntity defConfig = 
				configRepository.findById(new BigInteger(configId)).get();

		if (defConfig.getComponentConfig() == null) {
			defConfig.setComponentConfig(new ComponentConfig());
		}

		if (json.get("baseElementName") != null) {
			String baseTempName = json.get("baseElementName").getAsString();
			FormTemplate formTemp = getFormTemplate(defConfig, baseTempName);

			if (formTemp == null) {
				formTemp = new FormTemplate();

				formTemp.setBaseTemplateName(baseTempName);
				formTemp.setTemplateName(json.get("formElementName").getAsString());

				defConfig.getFormTemplates().add(formTemp);
			}

			if(json.get("formContent") != null) {
				formTemp.setTemplateCode(json.get("formContent").getAsString());
			}
			
			if(json.get("templateType") != null) {
				formTemp.setTemplateType(json.get("templateType").getAsString());
			}
		}

		
		if(json.get("defaultConfigJson") != null) {
			defConfig.setDefaultConfigJson(json.get("defaultConfigJson").getAsString());
		}
		
		if(json.get("angCompPrefix") != null) {
			String angCompPrefix = json.get("angCompPrefix").getAsString();
			defConfig.getComponentConfig().setComponentPrefix(angCompPrefix);
			
			if (StringUtils.isNotBlank(defConfig.getDefaultConfigJson())) {
				JsonObject defJson = new Gson().fromJson(defConfig.getDefaultConfigJson(), JsonObject.class);
				defJson.get("componentConfig").getAsJsonObject().addProperty("componentPrefix", angCompPrefix);

				defConfig.setDefaultConfigJson(new Gson().toJson(defJson));
			}
		}
		
		if(json.get("angCompStyleType") != null) {
			String angCompStyleType = json.get("angCompStyleType").getAsString();
			defConfig.getComponentConfig().setStyleExt(angCompStyleType);
						
			if (StringUtils.isNotBlank(defConfig.getDefaultConfigJson())) {
				JsonObject defJson = new Gson().fromJson(defConfig.getDefaultConfigJson(), JsonObject.class);
				defJson.get("componentConfig").getAsJsonObject().addProperty("styleExt", angCompStyleType);

				defConfig.setDefaultConfigJson(new Gson().toJson(defJson));
			}
		}
		
		
		configRepository.save(defConfig);
	}
	
	@RequestMapping(value = "/importFiles", method = RequestMethod.POST)
	@ResponseBody
	public void importFiles(@RequestBody String jsonLayout) {
		
		JsonObject json = new Gson().fromJson(jsonLayout, JsonObject.class);
		
		ResourceEntity entity = new ResourceEntity();
		if(json.get("fileId") != null) {
			entity.setId(json.get("fileId").getAsBigInteger());
		}
		
		if(json.get("fileName") != null) {
			entity.setFileName(json.get("fileName").getAsString());
		}
		
		if(json.get("fileType") != null) {
			entity.setFileType(json.get("fileType").getAsString());
		}
		
		if(json.get("fileContent") != null) {
			entity.setFileContent(json.get("fileContent").getAsString());
		}
		
		resourceRepository.save(entity);
	}
	
	@RequestMapping(value = "/deleteFiles", method = RequestMethod.POST)
	@ResponseBody
	public void deleteFiles(@RequestBody String jsonLayout) {
		
		JsonObject json = new Gson().fromJson(jsonLayout, JsonObject.class);
		BigInteger fileId = null;
		if(json.get("fileId") != null) {
			fileId = json.get("fileId").getAsBigInteger();
		}
		
		if (fileId != null) {
			resourceRepository.deleteById(fileId);
		}
	}
	
	@RequestMapping(value = "/mapResourceFiles", method = RequestMethod.POST)
	@ResponseBody
	public void mapResourceFiles(@RequestBody String jsonLayout) {
		
		JsonObject json = new Gson().fromJson(jsonLayout, JsonObject.class);
		
		String configId = json.get("id").getAsString();
		ConfigEntity defConfig = 
				configRepository.findById(new BigInteger(configId)).get();
		
		String fileId = null;
		if(json.get("fileName") != null) {
			fileId = json.get("fileName").getAsString();
		}
		
		ResourceTemplate template = null;
		if(defConfig.getResourceTemplates() != null && defConfig.getResourceTemplates().isEmpty() == false) {
			List<ResourceTemplate> resourceList = defConfig.getResourceTemplates();
			
			for (ResourceTemplate resourceTemplate : resourceList) {
				if (StringUtils.equalsIgnoreCase(resourceTemplate.getFileId(), fileId)) {
					template = resourceTemplate;
					break;
				}
			}
		} else {
			defConfig.setResourceTemplates(new ArrayList<>());
		}

		if (template == null) {
			template = new ResourceTemplate();
			defConfig.getResourceTemplates().add(template);
		}
		
		template.setFileId(fileId);
		if(json.get("filePath") != null) {
			template.setFilePath(json.get("filePath").getAsString());
		}
		
		configRepository.save(defConfig);
	}
	
	
	@RequestMapping(value = "/deleteResourceFiles", method = RequestMethod.POST)
	@ResponseBody
	public void deleteResourceFiles(@RequestBody String jsonLayout) {
		
		JsonObject json = new Gson().fromJson(jsonLayout, JsonObject.class);
		
		String configId = json.get("id").getAsString();
		ConfigEntity defConfig = 
				configRepository.findById(new BigInteger(configId)).get();
		
		String fileId = null;
		if(json.get("fileName") != null) {
			fileId = json.get("fileName").getAsString();
		}
		
		if(defConfig.getResourceTemplates() != null && defConfig.getResourceTemplates().isEmpty() == false) {
			List<ResourceTemplate> resourceList = defConfig.getResourceTemplates();
			
			for (Iterator<ResourceTemplate> iterator = resourceList.iterator(); iterator.hasNext();) {
				ResourceTemplate resourceTemplate = iterator.next();
				
				if (StringUtils.equalsIgnoreCase(resourceTemplate.getFileId(), fileId)) {
					iterator.remove();
					break;
				}
			}
		} 
		
		configRepository.save(defConfig);
	}
	
	@RequestMapping(value = "/deleteForm", method = RequestMethod.POST)
	@ResponseBody
	public void deleteForm(@RequestBody String jsonLayout) {
		
		JsonObject json = new Gson().fromJson(jsonLayout, JsonObject.class);
		
		String configId = json.get("configId").getAsString();
		ConfigEntity defConfig = 
				configRepository.findById(new BigInteger(configId)).get();

		if (json.get("baseElementName") != null) {
			String baseTempName = json.get("baseElementName").getAsString();
			
			if (defConfig.getFormTemplates() != null) {
				for (Iterator<FormTemplate> iterator = defConfig.getFormTemplates().iterator(); iterator.hasNext();) {
					FormTemplate form = iterator.next();
					if (form.getBaseTemplateName().equalsIgnoreCase(baseTempName)) {
						iterator.remove();
					}
				}
			}
		}
		
		configRepository.save(defConfig);
	}
	
	private FormTemplate getFormTemplate(ConfigEntity defConfig, String baseTempName) {
		if (defConfig.getFormTemplates() == null) {
			defConfig.setFormTemplates(new ArrayList<>());
		}

		FormTemplate retVal = null;
		for (FormTemplate form : defConfig.getFormTemplates()) {
			if (form.getBaseTemplateName().equalsIgnoreCase(baseTempName)) {
				retVal = form;
				break;
			}
		}
		
		return retVal;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public void saveLayout(@RequestParam("key") String key,
			@RequestBody String jsonLayout) {
		
		JsonObject json = new Gson().fromJson(jsonLayout, JsonObject.class);
		
		String type = json.get("type").getAsString();
		List<LayoutEntity> entityList = layoutRepository.findByLayoutNameAndType(key, type);
		LayoutEntity entity = null;
		if (entityList == null || entityList.isEmpty()) {
			entity = new LayoutEntity();
		} else {
			entity = entityList.get(0);
		}
		
		entity.setLayout(json.get("designJson").getAsString());
		entity.setLayoutName(key);
		entity.setType(json.get("type").getAsString());
		
		if (json.get("params") != null && json.get("params").isJsonArray()) {
			Type collectionType = new TypeToken<List<Params>>() { }.getType();
			List<Params> params = new Gson().fromJson(json.get("params"), collectionType);;

			entity.setParams(params);
		} else {
			entity.setParams(new ArrayList<>());
		}
		
		layoutRepository.save(entity);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public void deleteLayout(@RequestBody String jsonLayout) {
		
		JsonObject json = new Gson().fromJson(jsonLayout, JsonObject.class);
		
		String layoutId = json.get("layoutId").getAsString();
		
		LayoutEntity entity = layoutRepository.findById(new BigInteger(layoutId)).get();
		
		layoutRepository.delete(entity);
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
		codeGenRequest.setAppName("uimodeller");
		codeGenRequest.setUiName(codeGenRequest.getScreenName());
		codeGenRequest.setCss("component.css");
		codeGenRequest.setSpringBootConfigName("springboot-default");
		
		if(StringUtils.isEmpty(codeGenRequest.getGenLoginScreen())) {
			codeGenRequest.setGenLoginScreen("true");
		}
		
		String codeGenResponse = null;
		//layoutDao.callCodeGenerator(codeGenRequest.getScreenName(), codeGenRequest);
		
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
