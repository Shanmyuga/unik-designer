package com.cognizant.unik.data.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
//import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.cognizant.unik.data.model.CodeGenRequest;
import com.cognizant.unik.data.model.LabelValueBean;
@Repository
public class LayoutDAOImpl implements LayoutDAO {

	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Value("${angular.tool.path}")
	private String angularToolPath;
	
	@Value("${react.tool.path}")
	private String reactToolPath;
	
	public void saveLayout(String key, String value) {
		int count = this.jdbcTemplate.queryForObject(
		        "select count(*) from module_data where module_name = ?", Integer.class, key);
		if(count ==0 ) {
		this.jdbcTemplate.update(
		        "insert into module_data (module_name, module_data) values (?, ?)",
		        key, value);
		} else {
			this.jdbcTemplate.update(
			        "update module_data set module_data = ? where module_name = ?",
			        value, key);
		}

	}

	public String getLayout(String key) {
		// TODO Auto-generated method stub
		return this.jdbcTemplate.queryForObject(
		        "select module_data from module_Data where module_name = ?",
		        new Object[]{key}, String.class);
	}

	public List<LabelValueBean> getAllLayouts() {
		// TODO Auto-generated method stub
		List<LabelValueBean> beans = this.jdbcTemplate.query(
		        "select module_name, module_data from module_data",
		        new RowMapper<LabelValueBean>() {
		            public LabelValueBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		            	LabelValueBean bean = new LabelValueBean();

		    			bean.setDisplay(rs.getString(1));
		    			bean.setValue(rs.getString(2));
		              
		                return bean;
		            }
		        });
		
		return beans;
	}
	public void saveAnswers(String key, String jsonlayout) {
		
		int count = this.jdbcTemplate.queryForObject(
		        "select count(*) from form_answers where form_name = ?", Integer.class, key);
		if(count ==0) {
			this.jdbcTemplate.update(
			        "insert into form_answers (form_name, form_answers,updated_date,status) values (?, ?,sysdate,'P')",
			        key, jsonlayout);

		}
		else {
			this.jdbcTemplate.update( "update form_answers set form_answers = ? where form_name = ?",
			        jsonlayout, key);
		}
	}
	public String getAnswers(String key) {
		int count = this.jdbcTemplate.queryForObject(
		        "select count(*) from form_answers where form_name = ?", Integer.class, key);
		if(count >0) {
		System.out.println(key);
		return this.jdbcTemplate.queryForObject(
		        "select form_answers from form_answers where form_name = ?",
		        new Object[]{key}, String.class);
		}
		else {
			return null;
		}
	}
	public String getRuleDecisions(String key) {
		int count = this.jdbcTemplate.queryForObject(
		        "select count(*) from form_answers where form_name = ?", Integer.class, key);
		if(count > 0) {
		return this.jdbcTemplate.queryForObject(
		        "select rule_Decisions from form_answers where form_name = ?",
		        new Object[]{key}, String.class);
		}
		else {
			return null;
		}
	}
	public void updateRuleDecisions(String key, String value) {
		// TODO Auto-generated method stub
		this.jdbcTemplate.update( "update form_answers set rule_decisions = ? where form_name = ?",
		        value, key);
	}

	@Override
	public String callCodeGenerator(String layoutName, CodeGenRequest codeGenRequest) {
		System.out.println("Entry -> LayoutDAOImpl -> callCodeGenerator");
		String toolResponse =StringUtils.EMPTY;
		String response =StringUtils.EMPTY;
		String json=this.jdbcTemplate.queryForObject(
		        "select module_data from module_Data where module_name = ?",
		        new Object[]{layoutName}, String.class);
		
		if(!StringUtils.isEmpty(json)){
			codeGenRequest.setUiLayout(json);
			System.out.println("Request to UI code Generation Tool ******   "+ codeGenRequest.toString() );
			ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			HttpEntity<CodeGenRequest> request = new HttpEntity<>(codeGenRequest);
			
			if ("React".equalsIgnoreCase(codeGenRequest.getUiTechnology())) {
				toolResponse = restTemplate.postForObject(reactToolPath, request, String.class);
			} else {
				toolResponse = restTemplate.postForObject(angularToolPath, request, String.class);
			}
			
			System.out.println("Response from UI code Generation Tool ******   "+toolResponse);
			if(StringUtils.isNotEmpty(toolResponse)&& toolResponse.equalsIgnoreCase("Success!")) {
				response="Your " + codeGenRequest.getUiTechnology() + " application is ready for use !!";
			}
				
		}
		System.out.println("Exit -> LayoutDAOImpl -> callCodeGenerator");
		return response;
	}
	
	@Override
	public String invokeCodeGenerator(CodeGenRequest codeGenRequest) {
		System.out.println("Entry -> LayoutDAOImpl -> callCodeGenerator");
		String toolResponse = StringUtils.EMPTY;
		String response = StringUtils.EMPTY;
		
		if(!StringUtils.isEmpty(codeGenRequest.getUiLayout())){
			System.out.println("Request to UI code Generation Tool ******   "+ codeGenRequest.toString() );
			ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			HttpEntity<CodeGenRequest> request = new HttpEntity<>(codeGenRequest);
			
			if ("React".equalsIgnoreCase(codeGenRequest.getUiTechnology())) {
				toolResponse = restTemplate.postForObject(reactToolPath, request, String.class);
			} else {
				toolResponse = restTemplate.postForObject(angularToolPath, request, String.class);
			}
			
			System.out.println("Response from UI code Generation Tool ******   "+toolResponse);
			if(StringUtils.isNotEmpty(toolResponse)&& toolResponse.equalsIgnoreCase("Success!")) {
				response="Your " + codeGenRequest.getUiTechnology() + " application is ready for use !!";
			}
				
		}
		System.out.println("Exit -> LayoutDAOImpl -> callCodeGenerator");
		return response;
	}
}
