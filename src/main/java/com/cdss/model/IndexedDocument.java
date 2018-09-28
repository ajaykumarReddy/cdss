package com.cdss.model;

import javax.persistence.Entity;
import com.cdss.util.PortalParam;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
		@NamedQuery(name = PortalParam.FIND_BY_NOT_PROCESSED, query = "SELECT i FROM IndexedDocument i where i.processed = :processed"), })
public class IndexedDocument {

	private Long id;

	private String dockId;

	private String indexName;

	private String indexType;

	private boolean processed;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public String getDockId() {
		return dockId;
	}

	public void setDockId(String dockId) {
		this.dockId = dockId;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

}
