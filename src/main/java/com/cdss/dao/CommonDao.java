package com.cdss.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Singleton;
import javax.interceptor.AroundTimeout;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.cdss.model.Document;
import com.cdss.model.IndexedDocument;

@RequestScoped
public class CommonDao {

	@PersistenceContext
	EntityManager em;

	public void saveDocument(Document document) {
		em.persist(document);
	}

	public void saveIndexedDocument(IndexedDocument indexedDocument) {
		em.persist(indexedDocument);
	}

	public <T> List<T> findByNamedQuery(String namedQueryName, Map<String, Object> parameters) {
		try {
			Set<Entry<String, Object>> rawParameters = parameters.entrySet();
			Query query = this.em.createNamedQuery(namedQueryName);
			for (Entry<String, Object> entry : rawParameters) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
			return query.getResultList();
		} catch (Exception e) {

		}
		return null;
	}

}
