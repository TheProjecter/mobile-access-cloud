package com.google.cloud.client.po;

public class StoragePO {
	private String id = null;
	private String name = null;
	private String userName = null;
	private String group = null;
	private String description = null;
	private String type = null;
	private String size = null;
	private String userId = null;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return getStringOrEmpty(this.name);
	}
	public void setName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return getStringOrEmpty(userName);
	}
	public void setUserName(String user) {
		this.userName = user;
	}
	public String getGroup() {
		return getStringOrEmpty(group);
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getDescription() {
		return getStringOrEmpty(description);
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return getStringOrEmpty(type);
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSize() {
		return getStringOrEmpty(size);
	}
	public void setSize(String size) {
		this.size = size;
	}
	private String getStringOrEmpty(String value)
	{
		if(value==null)
		{
			value="";
		}
		return value;
	}
	public void setUserId(String idString) {
		this.userId = idString;
	}
	public String getUserId()
	{
		return this.userId;
	}
}
