package com.google.cloud.client.po;

public class BasePropertyPO {
	private String key;
	private String value;
	private boolean isVisible = true;
	public BasePropertyPO(String key, String value)
	{
		this.key = key;
		this.value = value;
	}
	public String getKey()
	{
		return this.key;
	}
	public String getValue()
	{
		return this.value;
	}
	public boolean getIsVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
