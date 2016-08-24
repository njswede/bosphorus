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
	
	private static final Log log = LogFactory.getLog(VraToken.class);
	
	public VraToken(String vraUrl, String tenant, String user, String password) throws ClientProtocolException, IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException {
		vraClient = new VraClient(vraUrl, tenant, user, password, true);
		this.user = user;
		log.debug("Created new token for user " + user);
	}

	@Override
	public String getName() {
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCredentials() {
		return vraClient.getAuthToken();
	}

	@Override
	public Object getDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.getName();
	}

	@Override
	public boolean isAuthenticated() {
		return vraClient != null && !vraClient.isExpired();
	}

	@Override
	public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
		throw new IllegalArgumentException("Not settable!");
	}
	
	public VraClient getVraClient() {
		return vraClient;
	}

}
