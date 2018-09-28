package com.cdss.util;

import javax.enterprise.context.RequestScoped;

import com.cdss.dto.DocumentData;
import com.google.gson.JsonObject;

@RequestScoped
public class FileIndexData {
	public boolean processFileContent(String fileName) {
		DocumentData jobDocument = new ElasticSearchUtil().findDocumentById(PortalParam.FS_JOBNAME,
				PortalParam.FS_JOB_TYPE, fileName);
		JsonObject jsonObject = new ElasticSearchUtil().findDocumentByIdAsObject(PortalParam.DOCUMENT_INDEX,
				PortalParam.DOCUMENT_INDEX_TYPE, fileName);
		JsonObject jsonObjectSource = jsonObject.getAsJsonObject("_source");
		if (jobDocument != null) {
			jsonObjectSource.addProperty("content", jobDocument.getContent());
			new ElasticSearchUtil().indexDocumentForIndexWithId(PortalParam.DOCUMENT_INDEX,
					PortalParam.DOCUMENT_INDEX_TYPE, jsonObjectSource.toString(), fileName);
			return true;
		}
		return false;
	}
}
