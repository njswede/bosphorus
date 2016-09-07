package com.vmware.samples.bosphorus.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.vmware.samples.bosphorus.live.LiveEvent;
import com.vmware.samples.bosphorus.live.LiveListener;
import com.vmware.samples.bosphorus.live.LiveUpdateBroker;

@RestController
@Scope("session")
public class LiveUpdateController  {
	@Autowired
	private LiveUpdateBroker liveUpdateBroker;
	
	private long lastId = Long.MAX_VALUE;

	private class Listener implements LiveListener {
		private final DeferredResult<LiveEvent> result;	
		
		public Listener(DeferredResult<LiveEvent> result) {
			super();
			this.result = result;
		}

		@Override
		public void onEvent(LiveEvent event, long eventId) {
			result.setResult(event);
			liveUpdateBroker.unsubscribe(this);
			lastId = eventId;
		}
	}
	
	@RequestMapping(value="/events", method=RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public DeferredResult<LiveEvent> pollEvent(@RequestParam String type, @RequestParam String subtype, 
			@RequestParam Long timeout) {
		DeferredResult<LiveEvent> result = new DeferredResult<LiveEvent>(timeout != null ? timeout : 5000L, null);
		LiveEvent pendingEvent = liveUpdateBroker.getFirstEventSince(lastId + 1);
		if(pendingEvent != null) {
			result.setResult(pendingEvent);
			lastId = pendingEvent.getEventId();
		}
		liveUpdateBroker.subscribe(new Listener(result), type, subtype);
		return result;
	}
	
	@RequestMapping(value="/events", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public void postEvent(@RequestBody LiveEvent event) {
		liveUpdateBroker.postEvent(event);
	}
}
