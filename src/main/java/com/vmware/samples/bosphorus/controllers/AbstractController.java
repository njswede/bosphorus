package com.vmware.samples.bosphorus.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vmware.samples.bosphorus.security.VraToken;
import com.vmware.samples.bosphorus.vra.VraClient;

public class AbstractController {

	public AbstractController() {
		super();
	}
	
	@Scope("session")
	protected VraClient getVra() {
		 VraToken auth = (VraToken) SecurityContextHolder.getContext().getAuthentication();
		 try { 
			 return auth.getVraClient();
		 } catch(Exception e) {
			 // Exceptions here are unrecoverable, so throw a RuntimeException
			 //
			 throw new RuntimeException(e);
		 }
	}
}