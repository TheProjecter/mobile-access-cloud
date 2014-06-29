package com.google.cloud.client.objects;

public enum InstanceType {
	SMALL("small"),
	MEDIUM("medium"),
	BIG("big");
	private String key;
	private InstanceType(String key)
	{
		this.key = key;
	}
	public String getType()
	{
		return key;
	}
}
