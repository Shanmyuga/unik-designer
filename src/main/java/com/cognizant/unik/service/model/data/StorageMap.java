package com.cognizant.unik.service.model.data;

import java.util.HashMap;
import java.util.Map;

public class StorageMap {

	private static StorageMap mymap = new StorageMap();
	
	private Map<String,String> answerMap = new HashMap<String, String>();
	private StorageMap() {
		
	}
	
	public static StorageMap getInstance() {
		return mymap;
	}
	
	public void addtomap(String key,String value) {
		this.answerMap.put(key, value);
	}

	public Map<String, String> getAnswerMap() {
		return answerMap;
	}

	public void setAnswerMap(Map<String, String> answerMap) {
		this.answerMap = answerMap;
	}
}
