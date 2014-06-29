package com.google.cloud.client.objects;



public class User extends BaseElement {

	private UserQuota userQuota;
	private UserUsage userUsage;
	public User(String type, String userId, String name) {
		super(type, userId, name);	
	}
	public User(String type, String userId) {
		this(type, userId, "");
	}
	public void setUserQuota(UserQuota userQuota)
	{
		this.userQuota = userQuota;
	}
	public void setUserUsage(UserUsage userUsage)
	{
		this.userUsage = userUsage;
	}
	public UserUsage getUserUsage()
	{
		return this.userUsage;
	}
	public UserQuota getUserQuota(){
		return this.userQuota;
	}
}
