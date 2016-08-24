package com.vmware.samples.bosphorus.models;

import java.util.Collection;
import java.util.Map;

public class MachineTemplate {
	public static class FieldConfig {
		private final int defaultVal;
		
		private final int min;
		
		private final int max;

		public int getDefaultVal() {
			return defaultVal;
		}

		public int getMin() {
			return min;
		}

		public int getMax() {
			return max;
		}

		public FieldConfig(int defaultVal, int min, int max) {
			super();
			this.defaultVal = defaultVal;
			this.min = min;
			this.max = max;
		}
		
		private static int decodeFacet(Map<String, Map<String, Object>> facet) {
			try {
				return (Integer) facet.get("value").get("value");
			} catch(NullPointerException e) {
				return -1;
			}
		}
		
		@SuppressWarnings("unchecked")
		public static FieldConfig fromFacets(Collection<Map<String, Object>> facets) {
			int defaultVal = -1, min = -1, max = -1;
			for(Map<String, Object> facet : facets) {
				String type = (String) facet.get("type");
				if("defaultValue".equals(type))
					defaultVal = decodeFacet((Map<String, Map<String, Object>>) facet.get("value"));
				else if("minValue".equals(type))
					min = decodeFacet((Map<String, Map<String, Object>>) facet.get("value"));
				else if("maxValue".equals(type))
					max = decodeFacet((Map<String, Map<String, Object>>) facet.get("value"));
			}
			return max != -1 || min != -1 || defaultVal != -1 ? new FieldConfig(defaultVal, min, max) : null;
		}
	}
	
	private String name;
	
	private FieldConfig cpu;
	
	private FieldConfig ram;
	
	private FieldConfig storage;

	public MachineTemplate(String name, FieldConfig cpu, FieldConfig ram, FieldConfig storage) {
		super();
		this.name = name;
		this.cpu = cpu;
		this.ram = ram;
		this.storage = storage;
	}
	
	public String getName() {
		return name;
	}

	public FieldConfig getCpu() {
		return cpu;
	}

	public void setCpu(FieldConfig cpu) {
		this.cpu = cpu;
	}

	public FieldConfig getRam() {
		return ram;
	}

	public void setRam(FieldConfig ram) {
		this.ram = ram;
	}

	public FieldConfig getStorage() {
		return storage;
	}

	public void setStorage(FieldConfig storage) {
		this.storage = storage;
	}
}
