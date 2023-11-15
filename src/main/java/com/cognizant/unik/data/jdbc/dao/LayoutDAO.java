package com.cognizant.unik.data.jdbc.dao;

import java.util.List;

import com.cognizant.unik.data.model.CodeGenRequest;
import com.cognizant.unik.data.model.LabelValueBean;

public interface LayoutDAO {
	

	public void saveLayout(String key, String value);
	
	
	public String  getLayout(String key);
	
	public List<LabelValueBean> getAllLayouts();
	
	public void saveAnswers(String key, String jsonlayout);
	
	public String getAnswers(String key);
	
	public String getRuleDecisions(String key);
	
	public void updateRuleDecisions(String key, String value);
	
	public String callCodeGenerator(String layoutName, CodeGenRequest codeGenRequest);
	
	public String invokeCodeGenerator(CodeGenRequest codeGenRequest);
}
