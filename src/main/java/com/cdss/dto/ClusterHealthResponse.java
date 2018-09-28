package com.cdss.dto;

import java.io.Serializable;

import javax.enterprise.inject.Model;

@Model
public class ClusterHealthResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String clusterName;

	private String clusterState;

	private int numberOfNodes;

	private int primaryShards;

	private int replicaShards;

	private String totalSize;

	private int totalIndices;

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getClusterState() {
		return clusterState;
	}

	public void setClusterState(String clusterState) {
		this.clusterState = clusterState;
	}

	public int getNumberOfNodes() {
		return numberOfNodes;
	}

	public void setNumberOfNodes(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}

	public int getPrimaryShards() {
		return primaryShards;
	}

	public void setPrimaryShards(int primaryShards) {
		this.primaryShards = primaryShards;
	}

	public int getReplicaShards() {
		return replicaShards;
	}

	public void setReplicaShards(int replicaShards) {
		this.replicaShards = replicaShards;
	}

	public String getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}

	public int getTotalIndices() {
		return totalIndices;
	}

	public void setTotalIndices(int totalIndices) {
		this.totalIndices = totalIndices;
	}

}
