/**
 * Copyright (C) 2012 Gist Labs, LLC. (http://gistlabs.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gistlabs.mechanize;

import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;

import com.gistlabs.mechanize.util.*;

public class ContentPage extends Page {
	public static Collection<String> CONTENT_MATCHERS = 
		Collections.collection(
				ContentType.WILDCARD.getMimeType(), 
				ContentType.APPLICATION_OCTET_STREAM.getMimeType(), 
				ContentType.DEFAULT_BINARY.getMimeType(), 
				ContentType.DEFAULT_TEXT.getMimeType());
	
	public ContentPage(MechanizeAgent agent, HttpRequestBase request, HttpResponse response) {
		super(agent, request, response);
	}
}