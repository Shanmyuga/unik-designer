/**
 * 
 */
package com.cognizant.unik.data.model;

import java.util.List;

/**
 * @author 238209
 *
 */
public class ComponentBean {
	
	private String id;
	
	private String componentName;
	
	private String displayName;
	
	private boolean containerElement;
	
	private boolean hideElement;
	
	private int containerRowCount;
	
	private int containerColCount;
	
	private List<ComponentParam> componentParams;
	
	private List<ComponentProperty> componentProps;

	private String formDisplayTemplate;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the componentName
	 */
	public String getComponentName() {
		return componentName;
	}

	/**
	 * @param componentName the componentName to set
	 */
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the containerElement
	 */
	public boolean isContainerElement() {
		return containerElement;
	}

	/**
	 * @param containerElement the containerElement to set
	 */
	public void setContainerElement(boolean containerElement) {
		this.containerElement = containerElement;
	}

	/**
	 * @return the componentParams
	 */
	public List<ComponentParam> getComponentParams() {
		return componentParams;
	}

	/**
	 * @param componentParams the componentParams to set
	 */
	public void setComponentParams(List<ComponentParam> componentParams) {
		this.componentParams = componentParams;
	}

	/**
	 * @return the containerRowCount
	 */
	public int getContainerRowCount() {
		return containerRowCount;
	}

	/**
	 * @param containerRowCount the containerRowCount to set
	 */
	public void setContainerRowCount(int containerRowCount) {
		this.containerRowCount = containerRowCount;
	}

	/**
	 * @return the containerColCount
	 */
	public int getContainerColCount() {
		return containerColCount;
	}

	/**
	 * @param containerColCount the containerColCount to set
	 */
	public void setContainerColCount(int containerColCount) {
		this.containerColCount = containerColCount;
	}

	/**
	 * @return the componentProps
	 */
	public List<ComponentProperty> getComponentProps() {
		return componentProps;
	}

	/**
	 * @param componentProps the componentProps to set
	 */
	public void setComponentProps(List<ComponentProperty> componentProps) {
		this.componentProps = componentProps;
	}

	/**
	 * @return the formDisplayTemplate
	 */
	public String getFormDisplayTemplate() {
		return formDisplayTemplate;
	}

	/**
	 * @param formDisplayTemplate the formDisplayTemplate to set
	 */
	public void setFormDisplayTemplate(String formDisplayTemplate) {
		this.formDisplayTemplate = formDisplayTemplate;
	}

	/**
	 * @return the hideElement
	 */
	public boolean isHideElement() {
		return hideElement;
	}

	/**
	 * @param hideElement the hideElement to set
	 */
	public void setHideElement(boolean hideElement) {
		this.hideElement = hideElement;
	}
}
