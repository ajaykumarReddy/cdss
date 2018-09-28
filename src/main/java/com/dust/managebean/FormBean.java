package com.dust.managebean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;

@Model
public class FormBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> submittedValues = new ArrayList<>();

	private List<DocumentBean> documents = new ArrayList<DocumentBean>();

	private List<Object> dataset = new ArrayList<>();

	private String field;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public List<String> getSubmittedValues() {
		return submittedValues;
	}

	public void setSubmittedValues(List<String> submittedValues) {
		this.submittedValues = submittedValues;
	}

	public List<Object> getDataset() {
		return dataset;
	}

	public void setDataset(List<Object> dataset) {
		this.dataset = dataset;
	}

	public String fieldData(String json, String name) {

		return name;

	}

	public List<DocumentBean> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentBean> documents) {
		this.documents = documents;
	}

	public void parseParaGraph(String term) {
		for (DocumentBean documentBean : documents) {
			String content = "";
			String sentence;
			try {
				content = documentBean.getContent();
				sentence = content.substring(content.indexOf(term, 1), content.indexOf(".", content.indexOf(term) + 1));
			} catch (Exception e) {
				sentence = content;// TODO Auto-generated catch block
				e.printStackTrace();
			}
			documentBean.setSentence(sentence);
		}

	}
}
