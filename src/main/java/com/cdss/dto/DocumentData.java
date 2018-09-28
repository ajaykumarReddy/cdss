package com.cdss.dto;

import java.io.Serializable;

public class DocumentData implements Serializable {

	private String content;

	private String attachment;

	private Object meta;

	private Object file;

	private Object path;

	private Object attributes;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Object getMeta() {
		return meta;
	}

	public void setMeta(Object meta) {
		this.meta = meta;
	}

	public Object getFile() {
		return file;
	}

	public void setFile(Object file) {
		this.file = file;
	}

	public Object getPath() {
		return path;
	}

	public void setPath(Object path) {
		this.path = path;
	}

	public Object getAttributes() {
		return attributes;
	}

	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}

}
