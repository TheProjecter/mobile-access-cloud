package com.google.cloud.client.objects;

public enum ObjectType {
	NETWORK("network"),
	STORAGE("storage"),
	COMPUTE("compute"),
	USER("user"),
	INSTANCE_TYPE("instance_type"),
	DISK("disk"),
	NIC("nic");
	private String key;
	private ObjectType(String key)
	{
		this.key = key;
	}
	public String getType()
	{
		return this.key;
	}
}
