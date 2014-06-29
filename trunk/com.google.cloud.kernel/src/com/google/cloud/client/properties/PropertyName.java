package com.google.cloud.client.properties;

public enum PropertyName {
	NAME("name"),
	ADDRESS("address_property"),
	USERNAME("username_property"),
	USER_HREF("user_href_property"),
	SIZE("size_property"),
    GROUP("group_property"),
    PUBLIC("public_property"),
	USERID("userid_property"), 
	HREF("href_property"),
	INSTANCE_TYPE("instance_type_property"),
	INSTANCE_TYPE_HREF("instance_type_href"),
	INSTANCE_TYPE_NAME("instance_type_name"),
	STORAGE_HREF("storager_href_property"),
	STORAGE_NAME("storage_name_property"),
	NETWORK_HREF("network_href_property"),
	NETWORK_NAME("network_name_property"),
	COMPUTE_HREF("compute_href_property"),
	IP("ip_property"),
	MAC("mac_propertyy"),
	DISK_TYPE("disk_type_property"),
	DISK_TARGET("disk_target_property"),
	CPU("cpu_property"),
	MEMORY("memory_property"),
	STATE("state_property"),
	STORAGE_TYPE("storage_type_property"),
	STORAGE_PERSISTENT("storage_persistent_property"),
	STORAGE_FSTYPE("storage_fstype_property");
	
	private String type;
    private PropertyName(String type)
    {
    	this.type = type;
    }
	public String getType()
	{
		return type;
	}
}
