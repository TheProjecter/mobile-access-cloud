package com.google.cloud.client.po;

public class NetworkPO{
	private String id = null;
	private String userId = null;
	private String networkname = null;
	private String username = null;
	private String networkgroup= null;
	private String address = null;
	private String networksize = null;
	private Boolean ispublicelement = null;
	private String href = null;
	public String getId() {
		return checkIfNull(id);
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId()
	{
		return this.userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getUserName() {
		return checkIfNull(username);
	}
	public void setUserName(String username) {
		this.username = username;
	}
	private String checkIfNull(String value)
	{
		if(value==null)
		{
			value="";
		}
		return value;
	}
	public String getGroup() {
		return networkgroup;
	}
	public void setGroup(String group) {
		this.networkgroup = group;
	}
	public String getAddress() {
		return checkIfNull(address);
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSize() {
		return checkIfNull(networksize);
	}
	public void setSize(String size) {
		this.networksize = size;
	}
	public String getName() {
		return checkIfNull(networkname);
	}
	public void setName(String name) {
		name = checkIfNull(name);
		this.networkname = name;
	}
	public void setIsPublicElement(Boolean element)
	{
		this.ispublicelement = element;
	}
	public Boolean getIsPublicElement()
	{
		return ispublicelement;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
}
