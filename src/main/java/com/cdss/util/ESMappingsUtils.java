/**
 * 
 */
package com.cdss.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;

import com.cdss.model.Document;
import com.cdss.model.DocumentField;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RequestScoped
public class ESMappingsUtils {

	public String getMappingsForRepository(Document document) {
		return getMappingsNode(document).toString();
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

}
