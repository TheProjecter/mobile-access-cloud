package com.google.cloud.client.properties;


public class StringProperty extends Property<String> {

	public StringProperty(String key, String value) {
		this(key, value, false);
	}
	public StringProperty(String key, String value, Boolean isChangeAllowed)
	{
		super(key, value, isChangeAllowed);
	}
}
