package com.cdss.dto;

import java.util.ArrayList;
import java.util.List;

public class IndexingData {

	private String name;

	private String category;

	private List<Object> documents=new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<Object> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Object> documents) {
		this.documents = documents;
	}

}
