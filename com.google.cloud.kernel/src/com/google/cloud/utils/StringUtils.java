package com.google.cloud.utils;

import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.properties.StringProperty;

public class StringUtils {
	public static boolean isNullOrEmpty(String value)
	{
		if(value==null)
		{
			return true;
		}
		if(value.equals("") || value.length()==0)
		{
			return true;
		}
		return false;
	}
	public static boolean isTrimStringNullOrEmpty(String value)
	{
		if(!isNullOrEmpty(value))
		{
			return isNullOrEmpty(value.trim());
		}
		return false;
	}
	public static boolean isIntegerNullOrEmpty(Integer value)
	{
		if(value!=null)
		{
			return true;
		}
		return false;
	}
	public static String getKey(StringProperty prop)
	{
		String key = null;
		if(prop!=null)
		{
			key = prop.getKey();
		}
		if(key==null)
		{
			key = "";
		}
		return key;
	}
	public static String getName(BaseElement occiElement) {
		String name = null;
		if(occiElement!=null)
		{
			name = occiElement.getName();
		}
		if(isTrimStringNullOrEmpty(name))
		{
			name = "";
		}
		return name;
	}
	public static String getValue(StringProperty prop) {
		String value = null;
		if(prop!=null)
		{
			value = prop.getKey();
		}
		if(value==null)
		{
			value = "";
		}
		return value;
	}

}
