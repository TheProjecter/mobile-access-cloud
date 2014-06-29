package com.google.cloud.client.po;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.utils.ComparationUtils;

public class BaseElementPO {
	private String id;
	private String name;
	private ObjectType type;
	private List<BasePropertyPO> propertyList = new ArrayList<BasePropertyPO>();
	public BaseElementPO()
	{
		
	}
	public BaseElementPO(String id, String name, ObjectType type)
	{
		this.id = id;
		this.name = name;
		this.type = type;
	}
	public String getId()
	{
		return this.id;
	}
	public String getName()
	{
		return this.name;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public ObjectType getObjectType()
	{
		return type;
	}
	public void setObjectType(ObjectType type)
	{
		this.type = type;
	}
	private boolean propIsInTheList(BasePropertyPO propPO)
	{
		for(BasePropertyPO prop : propertyList)
		{
			if(ComparationUtils.areObjectsEqual(propPO, prop))
			{
				return true;
			}
		}
		return false;
	}
	public List<BasePropertyPO> getPropertyList()
	{
		return propertyList;
	}
	public void addPropertyToList(BasePropertyPO propPO)
	{
		if(propPO!=null && propertyList!=null)
		{
			if(!propIsInTheList(propPO))
			{
				propertyList.add(propPO);
			}
		}
	}
}
