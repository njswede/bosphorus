package com.vmware.samples.bosphorus.models;

public class MachineConfiguration {
	private int numCPUs;
	
	private int memoryMB;
	
	private int diskGB;
	
	private String reservationPolicyId;
	
	public MachineConfiguration() {
	}
	
	public MachineConfiguration(int numCPUs, int memoryMB, int diskGB, String reservationPolicyId) {
		super();
		this.numCPUs = numCPUs;
		this.memoryMB = memoryMB;
		this.diskGB = diskGB;
		this.reservationPolicyId = reservationPolicyId;
	}

	public int getNumCPUs() {
		return numCPUs;
	}

	public void setNumCPUs(int numCPUs) {
		this.numCPUs = numCPUs;
	}

	public int getMemoryMB() {
		return memoryMB;
	}

	public void setMemoryMB(int memoryMB) {
		this.memoryMB = memoryMB;
	}

	public int getDiskGB() {
		return diskGB;
	}

	public void setDiskGB(int diskGB) {
		this.diskGB = diskGB;
	}

	public String getReservationPolicyId() {
		return reservationPolicyId;
	}

	public void setReservationPolicyId(String reservationPolicyId) {
		this.reservationPolicyId = reservationPolicyId;
	}
	
	
}
