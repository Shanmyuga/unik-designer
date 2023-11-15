package com.cognizant.unik.data.model;

import java.util.ArrayList;
import java.util.List;

public class ComponentProperty {
	private String propertyName;
	private String propertyKey;
	private String propertyDataType;
	
	private List<LabelValueBean> options;

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the propertyDataType
	 */
	public String getPropertyDataType() {
		return propertyDataType;
	}

	/**
	 * @param propertyDataType the propertyDataType to set
	 */
	public void setPropertyDataType(String propertyDataType) {
		this.propertyDataType = propertyDataType;
	}

	/**
	 * @return the options
	 */
	public List<LabelValueBean> getOptions() {
		if (options == null) {
			options = new ArrayList<>();
		}
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(List<LabelValueBean> options) {
		this.options = options;
	}

	/**
	 * @return the propertyKey
	 */
	public String getPropertyKey() {
		return propertyKey;
	}

	/**
	 * @param propertyKey the propertyKey to set
	 */
	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}
}
