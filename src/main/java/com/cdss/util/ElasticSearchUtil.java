package com.cdss.util;

import java.io.IOException;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.cdss.dto.DocumentData;
import com.google.gson.JsonObject;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.cluster.Health;
import io.searchbox.cluster.Stats;
import io.searchbox.core.Bulk;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.PutMapping;

@RequestScoped
public class ElasticSearchUtil {

	private String elasticSearchUrl = PortalParam.ELASTIC_SEARCH_URL;

	private static JestClient getClient(String esEndpoint) {
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(esEndpoint).multiThreaded(true).build());
		return factory.getObject();
	}

	private JestClient getClient() {
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(elasticSearchUrl).multiThreaded(true).build());
		return factory.getObject();
	}

	public void createIndex(String indexName) {

		final JestClient client = getClient();
		try {

			final JestResult result = client.execute(new CreateIndex.Builder(indexName).build());
			if (!result.isSucceeded()) {
				// "index already created");
			}
		} catch (IOException e) {
			throw new RuntimeException("io exception occur");
		}
	}

	public void updateMappingForIndex(String indexName, String indexType, String mapping) {

		JestClient client = getClient();
		final PutMapping putMapping = new PutMapping.Builder(indexName, indexType, mapping).build();

		JestResult result;
		try {
			result = client.execute(putMapping);

			if (!result.isSucceeded()) {
				throw new RuntimeException("index mapping exception");
			}
		} catch (IOException e) {
			throw new RuntimeException("io exception occur");
		}

	}

	public void indexDocumentForIndex(String indexname, String type, Object source) {
		Bulk bulk = new Bulk.Builder().defaultIndex(indexname).defaultType(type)
				.addAction(new Index.Builder(source).build()).build();
		JestClient client = getClient();
		try {
			JestResult result = client.execute(bulk);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void indexDocumentForIndexWithId(String indexname, String type, Object source, String id) {
		JestClient client = getClient();
		try {
			JestResult result = client.execute(new Index.Builder(source).index(indexname).type(type).id(id).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object Serach(String query, String indexName, String indexType) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryString(query);
		searchSourceBuilder.query(queryStringQueryBuilder);
		Search searchElastic = (Search) new Search.Builder(searchSourceBuilder.toString())
				// multiple index or types can be added.
				.addIndex(indexName).addType(indexType).build();
		JestClient client = getClient();
		SearchResult elasticRs = null;
		try {
			elasticRs = client.execute(searchElastic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elasticRs.getSourceAsString();
	}

	public List<String> SerachListAsString(String query, String indexName, String indexType) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryString(query);
		searchSourceBuilder.query(queryStringQueryBuilder);
		Search searchElastic = (Search) new Search.Builder(searchSourceBuilder.toString())
				// multiple index or types can be added.
				.addIndex(indexName).addType(indexType).build();
		JestClient client = getClient();
		SearchResult elasticRs = null;
		try {
			elasticRs = client.execute(searchElastic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elasticRs.getSourceAsStringList();
	}

	public DocumentData findDocumentById(String indexName, String indexType, String id) {
		Get get = new Get.Builder(indexName, id).type(indexType).build();
		JestClient client = getClient();
		JestResult result = null;
		try {
			result = client.execute(get);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.getSourceAsObject(DocumentData.class);
	}

	public JsonObject findDocumentByIdAsObject(String indexName, String indexType, String id) {
		Get get = new Get.Builder(indexName, id).type(indexType).build();
		JestClient client = getClient();
		JestResult result = null;
		try {
			result = client.execute(get);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.getJsonObject();
	}

	public JsonObject getClusterHealthJson() {
		JestClient client = getClient();
		Health health = new Health.Builder().build();
		JestResult result = null;
		JsonObject resultJson = null;
		try {
			result = client.execute(health);
			if (!result.isSucceeded()) {
				throw new RuntimeException("elastic search not success");
			}
			resultJson = result.getJsonObject();

		} catch (IOException ioException) {
			throw new RuntimeException("elastic searhc io exception");
		}
		return resultJson;
	}

	public JsonObject clusterStatsJson() {
		JestClient client = getClient();
		JestResult result;
		JsonObject resultJson = null;
		try {
			result = client.execute(new Stats.Builder().build());
			if (!result.isSucceeded()) {
				throw new RuntimeException("elastic search not success");
			}
			resultJson = result.getJsonObject();

		} catch (IOException e) {
			throw new RuntimeException("elastic searhc io exception");
		}

		return resultJson;

	}
}
