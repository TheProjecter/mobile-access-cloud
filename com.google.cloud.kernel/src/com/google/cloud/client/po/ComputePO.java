package com.google.cloud.client.po;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.utils.ComparationUtils;

public class ComputePO {
	private String id = null;
	private String name = null;
	private ObjectType type;
	private List<DiskPO> diskList = null;
	private List<NicPO> nicList = null;
	private List<BasePropertyPO> propertyList = new ArrayList<BasePropertyPO>();
	public ComputePO()
	{
		this(null, null, ObjectType.COMPUTE);
	}
	public ComputePO(String id) {
		this(id, null, ObjectType.COMPUTE);
	}
	public ComputePO(String id, String name, ObjectType type)
	{
		this.id = id;
		this.name = name;
		this.type = type;
		nicList = new ArrayList<NicPO>();
		diskList = new ArrayList<DiskPO>();
	}
	
	public List<DiskPO> getDiskList() {
		return diskList;
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
	public void addNicPO(NicPO nicPO)
	{
		if(nicPO!=null && nicList!=null && !nicList.contains(nicPO))
		{
			nicList.add(nicPO);
		}
	}
	public void addDiskPO(DiskPO disk)
	{
		if(disk!=null && diskList!=null && !diskList.contains(disk))
		{
			diskList.add(disk);
		}
	}
	public List<NicPO> getNicList() {
		return nicList;
	}
	public String getId()
	{
		return this.id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public ObjectType getType()
	{
		return type;
	}
	public void setObjectType(ObjectType type) {
		this.type = type;
	}

}
