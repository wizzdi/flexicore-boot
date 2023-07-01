package com.flexicore.response;

import com.flexicore.model.DocumentationTag;

import java.util.List;

public class SwaggerTags {

	private final List<DocumentationTag> documentationTags;

	public SwaggerTags(List<DocumentationTag> documentationTags) {
		this.documentationTags = documentationTags;
	}

	public List<DocumentationTag> getDocumentationTags() {
		return documentationTags;
	}
}
