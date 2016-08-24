package com.vmware.samples.bosphorus.vra;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DateAwareMap extends HashMap<String, Object> {
	private static final long serialVersionUID = -4282248684231234366L;
	
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");


	public DateAwareMap() {
		super();
	}

	public DateAwareMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public DateAwareMap(int initialCapacity) {
		super(initialCapacity);
	}

	public DateAwareMap(Map<? extends String, ? extends Object> m) {
		super(m);
	}

	public Object get(Object key) {
		Object o = super.get(key);
		if(!(o instanceof String))
			return o;
		// If it walks like a Date and quacks like a Date... it's a Date!
		//
		try {
			return dateFormat.parseObject((String) o);
		} catch(ParseException e) {
			return o;
		}
	}
}
