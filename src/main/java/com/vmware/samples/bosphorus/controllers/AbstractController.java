package com.vmware.samples.bosphorus.controllers;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vmware.samples.bosphorus.security.VraToken;
import com.vmware.samples.bosphorus.vra.VraClient;

public class AbstractController {

	public AbstractController() {
		super();
	}
	
	protected VraClient getVra() {
		 VraToken auth = (VraToken) SecurityContextHolder.getContext().getAuthentication();
	     return auth.getVraClient();
	}
}