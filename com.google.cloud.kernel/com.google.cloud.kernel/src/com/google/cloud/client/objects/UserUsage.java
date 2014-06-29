package com.google.cloud.client.objects;

public class UserUsage {
	private String userId;
	private String cpu_used;
	private String memory_used;
	private String num_vms_used;
	private String storage_used;
	public UserUsage(String userId)
	{
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}
	public String getCpuUsed() {
		return cpu_used;
	}
	public void setCpuUsed(String cpu_used) {
		this.cpu_used = cpu_used;
	}
	public String getMemoryUsed() {
		return memory_used;
	}
	public void setMemoryUsed(String memory_used) {
		this.memory_used = memory_used;
	}
	public String getNumVmsUsed() {
		return num_vms_used;
	}
	public void setNumVmsUsed(String num_vms_used) {
		this.num_vms_used = num_vms_used;
	}
	public String getStorageUsed() {
		return storage_used;
	}
	public void setStorage_used(String storage_used) {
		this.storage_used = storage_used;
	}
}