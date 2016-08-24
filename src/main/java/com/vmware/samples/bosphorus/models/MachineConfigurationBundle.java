package com.vmware.samples.bosphorus.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineConfigurationBundle {
	private String blueprintId;
	
	private int lease;
	
	private String description;
	
	private Map<String, MachineConfiguration> machines;
	
	public MachineConfigurationBundle(String blueprintId, List<MachineTemplate> templates, int lease) {
		this.blueprintId = blueprintId;
		this.lease = lease;
		machines = new HashMap<String, MachineConfiguration>(templates.size());
		for(MachineTemplate t : templates) {
			machines.put(t.getName(), new MachineConfiguration(t.getCpu().getDefaultVal(), t.getRam().getDefaultVal(), t.getStorage().getDefaultVal(), null));
		}
	}

	public MachineConfigurationBundle() {
	}

	public Map<String, MachineConfiguration> getMachines() {
		return machines;
	}

	public void setMachines(Map<String, MachineConfiguration> machines) {
		this.machines = machines;
	}

	public String getBlueprintId() {
		return blueprintId;
	}

	public void setBlueprintId(String blueprintId) {
		this.blueprintId = blueprintId;
	}

	public int getLease() {
		return lease;
	}

	public void setLease(int lease) {
		this.lease = lease;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
