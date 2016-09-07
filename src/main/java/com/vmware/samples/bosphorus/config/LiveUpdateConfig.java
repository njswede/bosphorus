package com.vmware.samples.bosphorus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.vmware.samples.bosphorus.live.LiveUpdateBroker;

@Configuration
public class LiveUpdateConfig {
	private final LiveUpdateBroker brokerInstance = new LiveUpdateBroker();
	
	@Bean
	@Scope("singleton")
	LiveUpdateBroker liveUpdateBroker() {
		return brokerInstance;
	}
}
