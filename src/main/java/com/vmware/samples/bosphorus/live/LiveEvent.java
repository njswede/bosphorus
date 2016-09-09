package com.vmware.samples.bosphorus.live;

public class LiveEvent {
	
	private static final String CATALOG = "CATALOG";
	
	private static final String LIFECYCLE = "LIFECYCLE";
	
	private static final String NEW = "NEW";
	
	private static final String CHANGED = "CHANGED";
	
	private static final String DELETED = "DELETED";
		
	private String requestId;

	private String type;
	
	private String subtype;
	
	private long eventId;
	
	private String resourceId;
	
	public LiveEvent() {
	}

	public LiveEvent(String requestId, String resourceId, String type, String subtype) {
		super();
		this.requestId = requestId;
		this.type = type;
		this.subtype = subtype;
		this.resourceId = resourceId;
	}

	public String getRequestId() {
		return requestId;
	}

	public String getType() {
		return type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
}
