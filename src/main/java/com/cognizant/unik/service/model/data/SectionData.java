package com.cognizant.unik.service.model.data;

public class SectionData {

	
	private boolean isSectionActive;
	private boolean isDisabled;
	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}


	private String sectionName;

	public boolean isSectionActive() {
		return isSectionActive;
	}

	public void setSectionActive(boolean isSectionActive) {
		this.isSectionActive = isSectionActive;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	
	
	private String sectionID;

	public String getSectionID() {
		return sectionID;
	}

	public void setSectionID(String sectionID) {
		this.sectionID = sectionID;
	}
	
	
}
