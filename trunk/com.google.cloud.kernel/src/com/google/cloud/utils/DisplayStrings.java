package com.google.cloud.utils;

import com.google.cloud.client.properties.PropertyName;
import com.google.cloud.client.properties.StringProperty;
/**
 * this class is for UI.The purpose is an esthetic one. Instead of showing
 * in the Listview property_userid we will show, User Id as the key.
 * @author Andrei
 *
 */
public class DisplayStrings {
	
	public static String getRealKeyForProperty(StringProperty property)
	{
		if(property.getKey().equals(PropertyName.USERID.getType()))
		{
			return new String("User Id");
		}
		else if(property.getKey().equals(PropertyName.ADDRESS.getType()))
		{
			return new String("Address");
		}
		else if(property.getKey().equals(PropertyName.GROUP.getType()))
		{
			return new String("Group");
		}
		else if(property.getKey().equals(PropertyName.PUBLIC.getType()))
		{
			return new String("Public");
		}
		else if(property.getKey().equals(PropertyName.NAME.getType()))
		{
			return new String("Name");
		}
		else if(property.getKey().equals(PropertyName.USERNAME.getType()))
		{
			return new String("Username");
		}
		else if(property.getKey().equals(PropertyName.SIZE.getType()))
		{
			return new String("Size");
		}
		else if(property.getKey().equals(PropertyName.HREF.getType()))
		{
			return new String("Href");
		}
		else if(property.getKey().equals(PropertyName.STORAGE_HREF.getType()))
		{
			return new String("Storage Href");
		}
		else if(property.getKey().equals(PropertyName.STATE.getType()))
		{
			return new String("State");
		}
		else if(property.getKey().equals(PropertyName.STORAGE_FSTYPE.getType()))
		{
			return new String("Fs Type");
		}
		else if(property.getKey().equals(PropertyName.STORAGE_PERSISTENT))
		{
			return new String("Persistent");
		}
		else if(property.getKey().equals(PropertyName.NETWORK_HREF.getType()))
		{
			return new String("Network Href");
		}
		else if(property.getKey().equals(PropertyName.CPU.getType()))
		{
			return new String("Cpu");
		}
		else if(property.getKey().equals(PropertyName.MEMORY.getType()))
		{
			return new String("Memory");
		}
		else if(property.getKey().equals(PropertyName.USER_HREF.getType()))
		{
			return new String("User Href");
		}
		else if(property.getKey().equals(PropertyName.INSTANCE_TYPE_HREF.getType()))
		{
			return new String("Instance Type Href");
		}
		else if(property.getKey().equals(PropertyName.INSTANCE_TYPE_NAME.getType()))
		{
			return new String("Instance Type Size");
		}
		else if(property.getKey().equals(PropertyName.STORAGE_PERSISTENT.getType()))
		{
			return new String("Persistent");
		}
		else if(property.getKey().equals(PropertyName.STORAGE_TYPE.getType()))
		{
			return new String("Type");
		}
		else if(property.getKey().equals(PropertyName.COMPUTE_HREF.getType()))
		{
			return new String("Compute Href");
		}
		else return new String("None");
	}
	
}
