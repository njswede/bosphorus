package com.vmware.samples.bosphorus.security;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.vmware.samples.bosphorus.vra.VraClient;

public class VraToken implements Authentication {
	
	private static final long serialVersionUID = 3014845492903704270L;
		
	private VraClient vraClient;
	
	private String user;
	
	private String password;
	
	private String tenant;
	
	private String vraUrl;
	
	private static final Log log = LogFactory.getLog(VraToken.class);
	
	public VraToken(String vraUrl, String tenant, String user, String password) throws ClientProtocolException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException {
		vraClient = new VraClient(vraUrl, tenant, user, password, true);
		this.user = user;
		this.password = password;
		this.tenant = tenant;
		this.vraUrl = vraUrl;
		log.debug("Created new token for user " + user);
	}

	@Override
	public String getName() {
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return vraClient.getAuthToken();
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.getName();
	}

	@Override
	public boolean isAuthenticated() {
		// If we exist we're authenticated!
		//
		return true;
	}

	@Override
	public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
		throw new IllegalArgumentException("Not settable!");
	}
	
	public VraClient getVraClient() throws HttpException, KeyManagementException, ClientProtocolException, NoSuchAlgorithmException, KeyStoreException, IOException, URISyntaxException {
		return !vraClient.isExpired() ? vraClient : new VraClient(vraUrl, tenant, user, password, true);
	}
}
