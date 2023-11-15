package com.cognizant.unik.service.model.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cognizant.unik.data.jdbc.dao.ComponentRepository;
import com.cognizant.unik.data.jdbc.dao.ConfigRepository;
import com.cognizant.unik.data.jdbc.dao.LayoutDAO;
import com.cognizant.unik.data.jdbc.dao.LayoutRepository;
import com.cognizant.unik.data.jdbc.dao.ResourceRepository;
import com.cognizant.unik.data.model.ComponentBean;
import com.cognizant.unik.data.model.ComponentEntity;
import com.cognizant.unik.data.model.ConfigBean;
import com.cognizant.unik.data.model.ConfigEntity;
import com.cognizant.unik.data.model.FormTemplate;
import com.cognizant.unik.data.model.LabelValueBean;
import com.cognizant.unik.data.model.LayoutBean;
import com.cognizant.unik.data.model.LayoutEntity;
import com.cognizant.unik.data.model.ResourceBean;
import com.cognizant.unik.data.model.ResourceEntity;
import com.cognizant.unik.service.model.data.Options;


@Controller
@RequestMapping("/rest/datamodel")
public class ModelExtractor {
	
	@Autowired
	private LayoutRepository layoutRepository;
	
	@Autowired
	private ConfigRepository configRepository;
	
	@Autowired
	private ComponentRepository componentRepository;
	
	@Autowired
	private ResourceRepository resourceRepository;
	
	@Autowired
	private LayoutDAO layoutDao;
	
	@RequestMapping(value = "/data/{name}", method = RequestMethod.GET)
	@ResponseBody
	public String getModelData(@PathVariable String name, ModelMap model) {
		String datamodel = layoutDao.getLayout(name);

		return datamodel;
	}
	
	@RequestMapping(value = "/get/{name}", method = RequestMethod.GET)
	@ResponseBody
	public LayoutBean getLayoutData(@PathVariable String name, ModelMap model) {
		Optional<LayoutEntity> entity = layoutRepository.findById(new BigInteger(name)); 
		
		LayoutEntity layout = entity.get();
		
		LayoutBean bean = new LayoutBean();
		bean.setId(layout.getId().toString());
		bean.setLayout(layout.getLayout());
		bean.setLayoutName(layout.getLayoutName());
		bean.setParams(layout.getParams());
		bean.setType(layout.getType());
		
		return bean;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Options getModellist(ModelMap model) {
		List<LabelValueBean> maps = layoutDao.getAllLayouts();
		Options dataoptions = new Options();
		
		for (LabelValueBean obj : maps) {
			dataoptions.getOptions().add(obj);
		}

		return dataoptions;
	}
	
	@RequestMapping(value = "/component/{type}", method = RequestMethod.GET)
	@ResponseBody
	public String getComponentsList(@PathVariable String type) {
		List<ComponentEntity> componentList = componentRepository.findByComponentName(type);
		
		String retVal = "";
		if (componentList != null && componentList.isEmpty() == false) {
			retVal = componentList.get(0).getFormDisplayTemplate();
		}
		
		return retVal;
	}
	
	@RequestMapping(value = "/components/list", method = RequestMethod.GET)
	@ResponseBody
	public List<ComponentBean> getComponentsList(ModelMap model) {
		List<ComponentEntity> componentList = componentRepository.findAll();
		
		List<ComponentBean> beanList = new ArrayList<>();
		ComponentBean bean = null;
		for (ComponentEntity compEntity : componentList) {
			bean = new ComponentBean();
			BeanUtils.copyProperties(compEntity, bean);
			bean.setId(compEntity.getId().toString());
			
			if (compEntity.getId() != null) {
				bean.setId(compEntity.getId().toString());
			}
			
			beanList.add(bean);
		}
		
		return beanList;
	}
	
	@RequestMapping(value = "/files/list", method = RequestMethod.GET)
	@ResponseBody
	public List<ResourceBean> getFilesList(ModelMap model) {
		List<ResourceEntity> resourceList = resourceRepository.findAll();
		
		List<ResourceBean> beanList = new ArrayList<>();
		ResourceBean bean = null;
		for (ResourceEntity resEntity : resourceList) {
			bean = new ResourceBean();
			BeanUtils.copyProperties(resEntity, bean);
			bean.setId(resEntity.getId().toString());
			
			if (resEntity.getId() != null) {
				bean.setId(resEntity.getId().toString());
			}
			
			beanList.add(bean);
		}
		
		return beanList;
	}
	
	@RequestMapping(value = "/config/list", method = RequestMethod.GET)
	@ResponseBody
	public List<ConfigBean> getConfigList(ModelMap model) {
		List<ConfigEntity> configList = configRepository.findAll();
		
		List<ConfigBean> beanList = new ArrayList<>();
		ConfigBean bean = null;
		for (ConfigEntity configEntity : configList) {
			bean = new ConfigBean();
			BeanUtils.copyProperties(configEntity, bean);
			bean.setId(configEntity.getId().toString());
			
			if (configEntity.getDefaultConfigId() != null) {
				bean.setDefaultConfigId(configEntity.getDefaultConfigId().toString());
			}
			
			beanList.add(bean);
		}
		
		return beanList;
	}
	
	@RequestMapping(value = "/config/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ConfigBean getConfigById(@PathVariable String id, ModelMap model) {
		ConfigEntity configEntity = configRepository.findById(new BigInteger(id)).get();

		ConfigBean bean = new ConfigBean();
		BeanUtils.copyProperties(configEntity, bean);
		bean.setId(configEntity.getId().toString());

		if (configEntity.getDefaultConfigId() != null) {
			bean.setDefaultConfigId(configEntity.getDefaultConfigId().toString());
		}

		return bean;
	}
	
	@RequestMapping(value = "/defaultconfig/list", method = RequestMethod.GET)
	@ResponseBody
	public List<ConfigBean> getDefaultConfigList(ModelMap model) {
		List<ConfigEntity> configList = configRepository.findByDefaultConfig(true);
		
		Map<String, ComponentEntity> compEntityMap = new HashMap<>();
		List<ComponentEntity> componentList = componentRepository.findAll();
		for (ComponentEntity componentEntity : componentList) {
			compEntityMap.put(componentEntity.getComponentName(), componentEntity);
		}
		
		List<ConfigBean> beanList = new ArrayList<>();
		ConfigBean bean = null;
		for (ConfigEntity configEntity : configList) {
			bean = new ConfigBean();
			BeanUtils.copyProperties(configEntity, bean);
			bean.setId(configEntity.getId().toString());
			
			if (configEntity.getDefaultConfigId() != null) {
				bean.setDefaultConfigId(configEntity.getDefaultConfigId().toString());
			}
			
			boolean changed = addUpdateFormTemplates(configEntity, compEntityMap);
			if (changed) {
				configRepository.save(configEntity);
			}
			
			beanList.add(bean);
		}
		
		return beanList;
	}
	
	private boolean addUpdateFormTemplates(ConfigEntity configEntity, Map<String, ComponentEntity> compEntityMap) {

		boolean changed = false;
		List<String> existingTemplates = new ArrayList<>();
		
		List<FormTemplate> formTemplates = configEntity.getFormTemplates();
		for (Iterator<FormTemplate> iterator = formTemplates.iterator(); iterator.hasNext();) {
			FormTemplate formTemplate = iterator.next();
			
			ComponentEntity entity = compEntityMap.get(formTemplate.getBaseTemplateName());
			if (entity == null && "form".equalsIgnoreCase(formTemplate.getTemplateType())) {
				changed = true;
				iterator.remove();
			} else if (entity != null) {
				existingTemplates.add(entity.getComponentName());
			}
		}
		
		List<String> actualTemplates = new ArrayList<String>(compEntityMap.keySet());
		actualTemplates.removeAll(existingTemplates);
		
		if (actualTemplates.isEmpty() == false) {
			changed = true;
			
			for (String tempName : actualTemplates) {
				ComponentEntity entity = compEntityMap.get(tempName);
				if (entity != null) {
					FormTemplate ft = new FormTemplate();
					ft.setBaseTemplateName(entity.getComponentName());
					ft.setTemplateName(entity.getDisplayName());
					ft.setTemplateType("form");
					ft.setTemplateCode("");
					
					configEntity.getFormTemplates().add(ft);
				}
			}
		}
		
		return changed;
	}

	@RequestMapping(value = "/layouts", method = RequestMethod.GET)
	@ResponseBody
	public Options getLayoutList(ModelMap model) {
		List<LayoutEntity> layoutList = layoutRepository.findByType("Layout");
		
		Options dataoptions = new Options();
		
		LabelValueBean obj = null;
		for (LayoutEntity entity : layoutList) {
			obj = new LabelValueBean();
			obj.setDisplay(entity.getLayoutName());
			obj.setValue(entity.getId().toString());
			
			dataoptions.getOptions().add(obj);
		}

		return dataoptions;
	}
	
	@RequestMapping(value = "/compLists", method = RequestMethod.GET)
	@ResponseBody
	public Options getComponentsListView(ModelMap model) {
		List<LayoutEntity> layoutList = layoutRepository.findByType("Component");
		
		Options dataoptions = new Options();
		
		LabelValueBean obj = null;
		for (LayoutEntity entity : layoutList) {
			obj = new LabelValueBean();
			obj.setDisplay(entity.getLayoutName());
			obj.setValue(entity.getId().toString());
			
			dataoptions.getOptions().add(obj);
		}

		return dataoptions;
	}
	
	@RequestMapping(value = "/components", method = RequestMethod.GET)
	@ResponseBody
	public List<LayoutEntity> getComponentList(ModelMap model) {
		return layoutRepository.findByType("Component");
	}
}