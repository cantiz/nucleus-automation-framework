package com.attinad.automation.common;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class HttpClient {

	public static HttpClient httpClient = null;
	private CloseableHttpClient client;

	private HttpClient() {
		client = HttpClients.createDefault();
	}

	public static HttpClient getInstance() {
		if (httpClient == null) {
			httpClient = new HttpClient();
		}
		return httpClient;
	}

	public String post(String postUri, Map<String, String> headers, String bodyJson)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(postUri);
		httpPost.setHeaders(getHeaders(headers, httpPost));
		StringEntity entity = new StringEntity(bodyJson);
		httpPost.setEntity(entity);
		CloseableHttpResponse response = client.execute(httpPost);
		return EntityUtils.toString(response.getEntity());
	}

	public String get(String getUri, Map<String, String> headers) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(getUri);
		httpGet.setHeaders(getHeaders(headers, httpGet));
		CloseableHttpResponse response = client.execute(httpGet);
		return EntityUtils.toString(response.getEntity());
	}

	private Header[] getHeaders(Map<String, String> headers, HttpUriRequest request) {
		Header[] requestHeaders = new Header[headers.size()];
		int i = 0;
		for (Entry<String, String> header : headers.entrySet()) {
			if (header.getKey().equals(HttpHeaders.AUTHORIZATION) && !header.getValue().startsWith("Bearer")) {
				String[] credentials = header.getValue().split(":");
				requestHeaders[i++] = getAuthorizationHeader(credentials[0], credentials[1], request);
			} else {
				requestHeaders[i++] = new BasicHeader(header.getKey(), header.getValue());
			}

		}
		return requestHeaders;
	}

	private Header getAuthorizationHeader(String username, String password, HttpUriRequest request) {
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
		try {
			return new BasicScheme().authenticate(creds, request, null);
		} catch (AuthenticationException e) {
			return new BasicHeader(HttpHeaders.AUTHORIZATION, "");
		}
	}

}
