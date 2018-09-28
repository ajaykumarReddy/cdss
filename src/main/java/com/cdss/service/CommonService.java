package com.cdss.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.cdss.dao.CommonDao;
import com.cdss.dto.IndexingData;
import com.cdss.model.Document;
import com.cdss.model.DocumentField;
import com.cdss.model.IndexedDocument;
import com.cdss.util.ESMappingsUtils;
import com.cdss.util.ElasticSearchUtil;
import com.cdss.util.PortalParam;
import com.dust.managebean.DocumentBean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RequestScoped
@Transactional
public class CommonService {

	@Inject
	private ElasticSearchUtil elasticSearchUtil;

	@Inject
	private ESMappingsUtils esMappingsUtils;

	private String fileUploadLocation = PortalParam.FILE_UPLOAD_LOCATION;

	@Inject
	private CommonDao commonDao;

	public boolean createMeataData() {

		Document document = new Document();
		document.setName("document");
		document.setCategory("document");
		List<DocumentField> documentFields = new ArrayList<DocumentField>();

		DocumentField codeDocument = new DocumentField();
		codeDocument.setName("code");
		codeDocument.setType("text");
		documentFields.add(codeDocument);

		DocumentField shortTitleDocument = new DocumentField();
		shortTitleDocument.setName("shortTitle");
		shortTitleDocument.setType("text");
		documentFields.add(shortTitleDocument);

		DocumentField fullTitleDocument = new DocumentField();
		fullTitleDocument.setName("fullTitle");
		fullTitleDocument.setType("text");
		documentFields.add(fullTitleDocument);

		DocumentField editionDocument = new DocumentField();
		editionDocument.setName("edition");
		editionDocument.setType("text");
		documentFields.add(editionDocument);

		DocumentField versionDocument = new DocumentField();
		versionDocument.setName("version");
		versionDocument.setType("text");
		documentFields.add(versionDocument);

		DocumentField classificationDocument = new DocumentField();
		classificationDocument.setName("classification");
		classificationDocument.setType("text");
		documentFields.add(classificationDocument);

		DocumentField workingGroupDocument = new DocumentField();
		workingGroupDocument.setName("workingGroup");
		workingGroupDocument.setType("text");
		documentFields.add(workingGroupDocument);

		DocumentField remarksDocument = new DocumentField();
		remarksDocument.setName("remarks");
		remarksDocument.setType("text");
		documentFields.add(remarksDocument);

		DocumentField contentDocument = new DocumentField();
		contentDocument.setName("content");
		contentDocument.setType("text");
		documentFields.add(contentDocument);

		DocumentField custodianDocument = new DocumentField();
		custodianDocument.setName("custodian");
		custodianDocument.setType("text");
		documentFields.add(custodianDocument);

		for (DocumentField documentField : documentFields) {
			documentField.setDocument(document);
		}
		document.getDocumentFields().addAll(documentFields);
		String index = document.getName().toLowerCase() + "$" + document.getCategory().toLowerCase();
		document.setIndexName(index);
		commonDao.saveDocument(document);
		elasticSearchUtil.createIndex(index);
		System.out.println("called" + document.getDocumentFields().size());
		// elasticSearchUtil.updateMappingForIndex(index, document.getCategory(),
		// getMappingsNode(document).toString());

		return true;

	}

	private ObjectNode getMappingsNode(Document document) {
		List<DocumentField> documentFields = document.getDocumentFields();

		final ObjectMapper mapper = new ObjectMapper();
		final Map<String, JsonNode> elementMap = new HashMap<String, JsonNode>();

		for (DocumentField element : documentFields) {
			String element_name = element.getName();
			final ObjectNode nestedNode = mapper.createObjectNode();
			nestedNode.put("type", element.getType());
			elementMap.put(element_name, nestedNode);
		}

		final ObjectNode propertiesNode = mapper.createObjectNode();
		propertiesNode.put("dynamic", "strict");
		propertiesNode.putObject("properties").setAll(elementMap);
		return propertiesNode;
	}

	public void indexData(DocumentBean documentBean) {
		Map<Object, Object> map = new HashMap<>();
		map.put("classification", documentBean.getClassification());
		map.put("code", documentBean.getCode());
		map.put("custodian", documentBean.getCustodian());
		map.put("edition", documentBean.getEdition());
		map.put("fullTitle", documentBean.getFullTitle());
		map.put("remarks", documentBean.getRemarks());
		map.put("shortTitle", documentBean.getShortTitle());
		map.put("version", documentBean.getVersion());
		map.put("workingGroup", documentBean.getWorkingGroup());
		map.put("content", "");
		elasticSearchUtil.indexDocumentForIndexWithId("document$document", "document", map,
				documentBean.getFile().getFileName());

		String name = documentBean.getFile().getFileName();
		IndexedDocument indexedDocument = new IndexedDocument();
		indexedDocument.setDockId(name);
		indexedDocument.setProcessed(false);
		indexedDocument.setIndexName("document$document");
		indexedDocument.setIndexType("document");
		commonDao.saveIndexedDocument(indexedDocument);

		try {
			Files.copy(documentBean.getFile().getInputstream(), new File(fileUploadLocation, name).toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> globalSearch(String term) {
		return elasticSearchUtil.SerachListAsString(term, "document$document", "document");
	}

}
