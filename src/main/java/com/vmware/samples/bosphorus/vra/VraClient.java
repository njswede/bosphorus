package com.vmware.samples.bosphorus.vra;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VraClient {
	private HttpClient http;
	private HttpHost vraHost;
	private String bearerToken;
	private Date tokenExpiration;
	
	private static Log log = LogFactory.getLog(VraClient.class);
	
	private static long expirationTolerance = 120000;
	
	public VraClient(String url, String tenant, String username, String password, boolean trustSelfSigned) throws ClientProtocolException, IOException, URISyntaxException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, HttpException {
		if(trustSelfSigned) {
			SSLContextBuilder sslBld = new SSLContextBuilder();
			sslBld.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			http = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(sslBld.build())).build();
		}
		else
			http = HttpClients.createDefault();
		URI uri = new URI(url);
		vraHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		Map<String, String> login = new HashMap<>();
		login.put("username", username);
		login.put("password", password);
		login.put("tenant", tenant);
		Map<String, Object> auth = this.post("/identity/api/tokens", JSONHelper.encode(login));
		bearerToken = "Bearer " + auth.get("id").toString();
		tokenExpiration = (Date) auth.get("expires");
		log.info("Successfully authenticated " + username + ". Session expires: " + tokenExpiration);
	}
	
	public String getAuthToken() {
		return bearerToken;
	}
	
	public boolean isExpired() {
		return System.currentTimeMillis() + expirationTolerance > tokenExpiration.getTime();
	}
	
	public Map<String, Object> post(String uri, String jsonContent) throws ClientProtocolException, IOException, HttpException {
		return this.toMap(this.postReturnString(uri, jsonContent));
	}
	
	public Map<String, Object> post(String uri, Map<String, Object> jsonContent) throws ClientProtocolException, IOException, HttpException {
		return this.post(uri, new ObjectMapper().writeValueAsString(jsonContent));
	}
	
	public String postReturnString(String uri, String jsonContent) throws ClientProtocolException, IOException, HttpException {
		HttpPost post = new HttpPost(uri);
		try {
			StringEntity payload = new StringEntity(jsonContent);
			payload.setContentType("application/json");
			post.setEntity(payload);
			post.setHeader("Accept", "application/json");
			if(bearerToken != null)
				post.setHeader("Authorization", bearerToken);
			log.debug("POST " + uri);
			return IOUtils.toString(this.checkResponse(http.execute(vraHost, post)).getEntity().getContent(), Charset.defaultCharset());
		} finally {
			post.releaseConnection();
		}	
	}
	
	public Map<String, Object> getPaged(String uri, String pattern, int start, int limit, OrderBy orderBy) throws ClientProtocolException, IOException, HttpException {
		uri += "?page=" + start + "&limit=" + limit;
		if(orderBy != null)
			uri += "&" + orderBy.toOdata();
		if(pattern != null)
			uri += "&filter=startswith(name,'" + pattern +"'";
		return this.get(uri);
	}
	
	public Map<String, Object> get(String uri) throws ClientProtocolException, IOException, HttpException {
		return this.toMap(this.getString(uri));
	}
	
	public String getString(String uri) throws UnsupportedOperationException, ClientProtocolException, IOException, HttpException {
		HttpGet get = new HttpGet(uri);
		try {
			if(bearerToken != null)
				get.setHeader("Authorization", bearerToken);
			get.setHeader("Accept", "application/json");
			log.debug("GET " + uri);
			return IOUtils.toString(this.checkResponse(http.execute(vraHost, get)).getEntity().getContent(), Charset.defaultCharset());
		} finally {
			get.releaseConnection();
		}
	}
	
	public byte[] getBinary(String uri, String contentType) throws UnsupportedOperationException, ClientProtocolException, IOException, HttpException {
		HttpGet get = new HttpGet(uri);
		try {
			if(bearerToken != null)
				get.setHeader("Authorization", bearerToken);
			get.setHeader("Accept", contentType);
			log.debug("GET " + uri);
			return IOUtils.toByteArray(this.checkResponse(http.execute(vraHost, get)).getEntity().getContent());
		} finally {
			get.releaseConnection();
		}
	}
	
	private HttpResponse checkResponse(HttpResponse response) throws HttpException, UnsupportedOperationException, IOException {
		int status = response.getStatusLine().getStatusCode();
		log.debug("HTTP status: " + status);
		if(status == 200 || status == 201)
			return response;
		log.debug("Error response from server: " + IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset()));
		throw new HttpException("HTTP Error: " + response.getStatusLine().getStatusCode() + " Reason: " + response.getStatusLine().getReasonPhrase());
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> toMap(String response) throws JsonParseException, JsonMappingException, UnsupportedOperationException, IOException {
		if(response == null || response.length() == 0) 
			return new HashMap<String, Object>();
		ObjectMapper om = new ObjectMapper();
		return JSONHelper.fixDates(om.readValue(response, Map.class));
	}
}
