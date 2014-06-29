package com.google.cloud.client.objects;

import java.util.UUID;


/**
 * Represents storage attached to an active VM
 * 
 * @author matthias
 */
public class AttachedStorage {
	
	private String id, type, target;
	private String storageHref;
	private String storageName;
	private String storageId;
	private BaseElement owner;
	public AttachedStorage()
	{
		this.id = String.valueOf(UUID.randomUUID());
	}
	public String getId() {
		return id;
	}
	public void setStorageId(String id)
	{
		this.storageId = id;
	}
	public BaseElement getOwner()
	{
		return owner;
	}
	public void setOwner(BaseElement owner)
	{
		this.owner = owner;
	}
	public String getStorageId()
	{
		return this.storageId;
	}
	public void setId(String id) {
		this.id = id;
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
	public void setStorageName(String name)
	{
		this.storageName = name;
	}
	public String getStorageName()
	{
		return this.storageName;
	}
	public String getStorageHref() {
		return storageHref;
	}

	public void setStorageHref(String storageHref) {
		this.storageHref = storageHref;
	}
}
