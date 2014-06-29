package com.google.cloud.client.objects;

public class UserQuota {
	private String userId;
	private String cpu_allowed;
	private String memory_allowed;
	private String num_wms_allowed;
	private String storage_allowed;
	public UserQuota(String userId)
	{
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	public String getMemoryAllowed() {
		return memory_allowed;
	}
	public void setMemoryAllowed(String memory_allowed) {
		this.memory_allowed = memory_allowed;
	}
	public String getNumVmsAllowed() {
		return num_wms_allowed;
	}
	public void setNumVmsAllowed(String num_wms_allowed) {
		this.num_wms_allowed = num_wms_allowed;
	}
	public String getStorageAllowed() {
		return storage_allowed;
	}
	public void setStorageAllowed(String storage_allowed) {
		this.storage_allowed = storage_allowed;
	}
	public String getCpuAllowed() {
		return cpu_allowed;
	}
	public void setCpuAllowed(String cpu_allowed) {
		this.cpu_allowed = cpu_allowed;
	}
	
}
