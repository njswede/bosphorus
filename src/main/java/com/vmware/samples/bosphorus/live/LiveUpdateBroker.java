package com.vmware.samples.bosphorus.live;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;

public class LiveUpdateBroker {
	private final WeakHashMap<LiveListener, LiveSubscription> listeners = new WeakHashMap<LiveListener, LiveSubscription>();
	
	private long currentId = 0;
	
	private static int MAX_BUFFER = 100;
	
	private TreeMap<Long, LiveEvent> buffer = new TreeMap<Long, LiveEvent>();
		
	public void subscribe(LiveListener subscriber, String type, String subtype) {
		synchronized(this) {
			listeners.put(subscriber, new LiveSubscription(type, subtype));
		}
	}
	
	public void unsubscribe(LiveListener subscriber) {
		synchronized(this) {
			listeners.remove(subscriber);
		}
	}
	
	public long getHwm() {
		synchronized(this) {
			return currentId;
		}
	}
	
	public void postEvent(LiveEvent e) {
		synchronized(this) {
			++currentId;
			e.setEventId(currentId);
			if(buffer.size() >= MAX_BUFFER)
				buffer.remove(buffer.firstKey());
			buffer.put(currentId, e);
			
			// Iterate over a copy to avoid concurrent iterator operations
			//
			Map<LiveListener, LiveSubscription> copy = new HashMap<LiveListener, LiveSubscription>(listeners);
			for(Map.Entry<LiveListener, LiveSubscription> entry : copy.entrySet()) {
				if(entry.getValue().matches(e))
					entry.getKey().onEvent(e, currentId);
			}
		}
	}
	
	public List<LiveEvent> getEventsSince(long hwm) {
		synchronized(this) {
			ArrayList<LiveEvent> result = new ArrayList<LiveEvent>();
			Map.Entry<Long, LiveEvent> current = buffer.ceilingEntry(hwm);
			while(current != null) {
				result.add(current.getValue());
				current = buffer.higherEntry(current.getKey());
			}
			return result;
		}
	}
	
	public LiveEvent getFirstEventSince(long hwm) {
		synchronized(this) {
			Map.Entry<Long, LiveEvent> current = buffer.ceilingEntry(hwm);
			return current != null ? current.getValue() : null;
		}
	}
}	
