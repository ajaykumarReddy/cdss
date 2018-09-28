package com.cdss.controller;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.cdss.dto.ClusterHealthResponse;
import com.cdss.service.CommonService;
import com.cdss.util.ElasticSearchUtil;
import com.dust.managebean.DocumentBean;
import com.dust.managebean.FormBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
@Model
public class FormController {

	@Inject
	private FormBean formBean;

	@Inject
	private DocumentBean documentBean;

	@Inject
	private ElasticSearchUtil elasticSearchUtil;

	@Inject
	private ClusterHealthResponse metricsBeat;

	@Inject
	private CommonService commonService;

	public void submit() {
		formBean.getDocuments().clear();
		formBean.getSubmittedValues().clear();
		formBean.getSubmittedValues().addAll(commonService.globalSearch(formBean.getField()));
		ObjectMapper mapper = new ObjectMapper();
		for (String document : formBean.getSubmittedValues()) {
			try {
				formBean.getDocuments().add(mapper.readValue(document, DocumentBean.class));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		formBean.parseParaGraph(formBean.getField());
		if (formBean.getSubmittedValues().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No results found."));
		}

	}

	public void add() {

		commonService.indexData(documentBean);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("added Successfully."));
	}

	public void reset() {
		formBean.getSubmittedValues().clear();
		formBean.setField(null);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Form reset."));
	}

	public ClusterHealthResponse getMetricsBeat() {
		JsonObject clusterHealthJson = elasticSearchUtil.getClusterHealthJson();
		ClusterHealthResponse clusterHealthResponse = new ClusterHealthResponse();
		clusterHealthResponse.setClusterName(clusterHealthJson.get("cluster_name").getAsString());
		clusterHealthResponse.setClusterState(clusterHealthJson.get("status").getAsString());
		clusterHealthResponse.setNumberOfNodes(clusterHealthJson.get("number_of_nodes").getAsInt());
		clusterHealthResponse.setPrimaryShards(clusterHealthJson.get("active_primary_shards").getAsInt());
		clusterHealthResponse.setPrimaryShards(clusterHealthJson.get("active_shards").getAsInt());

		return clusterHealthResponse;

	}

	public void createMetaData() {
		commonService.createMeataData();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("added MetaData Successfully."));
	}

}
