package com.google.cloud.client.po;

public class DiskPO {
	private String storageHref = null;
	private String storageName = null;
	private String type = null;
	private String target =null;
	private String storageId = null;
	public DiskPO()
	{
		
	}
	public String getStorageId()
	{
		return this.storageId;
	}
	public void setStorageId(String id)
	{
		this.storageId = id;
	}
	public String getStorageHref() {
		return storageHref;
	}
	public void setStorageHref(String storageHref) {
		this.storageHref = storageHref;
	}
	public String getStorageName() {
		return storageName;
	}
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
}
