package com.cognizant.unik.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecisionModel {

	
	public Map<String, List<String>> getDecisionMap() {
		return decisionMap;
	}

	public void setDecisionMap(Map<String, List<String>> decisionMap) {
		this.decisionMap = decisionMap;
	}

	private String decisionKey;
	
	private String decisionValue;
	
	private Map<String,List<String>> decisionMap = new HashMap<String,List<String>>();

	public String getDecisionKey() {
		return decisionKey;
	}

	public void setDecisionKey(String decisionKey) {
		this.decisionKey = decisionKey;
	}

	public String getDecisionValue() {
		return decisionValue;
	}

	public void setDecisionValue(String decisionValue) {
		this.decisionValue = decisionValue;
	}
	
	public void createDecision(String key,String value) {
		List<String> mylist = null;
		if(decisionMap.get(key)== null) {
			 mylist = new ArrayList<String>();
			 mylist.add(value);
		}
		else {
			mylist = decisionMap.get(key);
			mylist.add(value);
		}
		this.decisionMap.put(key, mylist);
	}
}
