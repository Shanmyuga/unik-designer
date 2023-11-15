package com.cognizant.unik.service.model.data;

import java.util.ArrayList;
import java.util.List;

public class QuestionServiceResponse {

	
	
	private List<SectionData> sectionData = new ArrayList<SectionData>();

	public List<SectionData> getSectionData() {
		return sectionData;
	}



	public void setSectionData(List<SectionData> sectionData) {
		this.sectionData = sectionData;
	}



	public String getQuestionLayout() {
		return questionLayout;
	}



	public void setQuestionLayout(String questionLayout) {
		this.questionLayout = questionLayout;
	}



	private String questionLayout;
}
