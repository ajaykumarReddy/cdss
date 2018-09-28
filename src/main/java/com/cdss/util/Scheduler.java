package com.cdss.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;

import com.cdss.dao.CommonDao;
import com.cdss.model.IndexedDocument;

@Startup
@Singleton
public class Scheduler {

	@Inject
	private CommonDao dao;

	@Inject
	private FileIndexData fileIndexData;

	@Resource
	TimerService timerService;

	@PostConstruct
	public void initialize() {
		timerService.createTimer(10000l, 5000l, "Delay 10 seconds then every 5 second timer");
	}

	@Timeout
	public void programmaticTimout(Timer timer) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(PortalParam.PROCESSED, false);
		List<IndexedDocument> documents = dao.findByNamedQuery(PortalParam.FIND_BY_NOT_PROCESSED, parameters);
		for (IndexedDocument indexedDocument : documents) {
			try {
				indexedDocument.setProcessed(fileIndexData.processFileContent(indexedDocument.getDockId()));
				dao.saveIndexedDocument(indexedDocument);
			} catch (Exception e) {
				indexedDocument.setProcessed(false);
				dao.saveIndexedDocument(indexedDocument);
			}

		}
	}
}
