package com.google.cloud.client.objects;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.client.properties.Property;
import com.google.cloud.client.properties.StringProperty;
import com.google.cloud.utils.ComparationUtils;


/**
 * Generic element for all the objects.
 * 
 * @author Andrei
 */
public class BaseElement {

	private String name;
	private String elementId;
	private boolean isElementUpdated = false;
	private String type;
	private boolean isVisible = true;
	private List<StringProperty> properties = new ArrayList<StringProperty>();
	/**
	 * Create a new OCCI Element which is connected to a client
	 * 
	 * @param client
	 */
	public BaseElement(String elementId, String type) {
		this(elementId, type, "");
	}
	public BaseElement(String elementId,String type,  String name)
	{
		this.type =  type;
		this.elementId = elementId;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String getObjectType()
	{
		return type;
	}
	public void setObjectType(String type)
	{
		this.type  = type;
	}
	public void addProperty(StringProperty property)
	{ 
		if(property!=null)
		{
			property.setOwner(this);
			properties.add(property);
		}
	}
	public void updatePropertyValue(StringProperty property)
	{
		for(StringProperty prop : this.properties)
		{
			if(prop.getKey().equals(property.getKey()))
			{
				prop.setValue(property.getValue());
				this.isElementUpdated = false;
			}
		}
	}
	public void addPropertyList(List<StringProperty> propertyList)
	{
		if(propertyList!=null && !propertyList.isEmpty() && properties!=null)
		{
			for(StringProperty prop : propertyList)
			{
				prop.setOwner(this);
				properties.add(prop);
			}
		}
	}
	public List<StringProperty> getPropertyList()
	{
		return properties;
	}
	public void deleteProperty(StringProperty property)
	{
		if(property!=null && properties!=null && !properties.isEmpty())
		{
			if(properties.contains(property))
			{
				properties.remove(property);
			}
		}
	}
	public void deletePropertyList(List<StringProperty> propertyList)
	{
		if(propertyList!=null && !propertyList.isEmpty())
		{
			if(properties!=null && !properties.isEmpty())
			{
				for(Property<?> property : propertyList)
				{
					if(properties.contains(property))
					{
						properties.remove(property);
					}
				}
			}
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public StringProperty getPropertyByKey(String key)
	{
		for(StringProperty prop : this.properties)
		{
			if(prop.getKey().equals(key))
			{
				return prop;
			}
		}
		return null;
	}
	@Override
	public String toString() {
		return "OCCIElement [name=" + name + "]";
	}
	/**
	 *this method is called when a new object has been received from the Web Client.
	 */
	public void update()
	{
		if(!isElementUpdated)
		{
			isElementUpdated = true;
			CloudKernelApplication.getInstance().getChangePublisher().notifyElementChanged(this);
		}
	}
	/**
	 * checks the equality of objects.
	 */
	@Override
	public boolean equals(Object obj) {
		// OCCI Elements are equal when their href matches
		if (obj instanceof BaseElement) {
			return internalEquals((BaseElement) obj);
		}
		return super.equals(obj);
	}

	protected boolean internalEquals(BaseElement other) {
		if (other != null) {
			if (this.getElementId() != null) {
				if (other.getElementId() == null) {
					return false;
				} else if (!this.getElementId().equals(other.getElementId())) {
					return false;
				}
			}
			if (this.getName() != null) {
				if (other.getName() == null) {
					return false;
				} else if (!this.getName().equals(other.getName())) {
					return false;
				}
			}
			if(this.getPropertyList()!=null && !this.getPropertyList().isEmpty())
			{
				if(other.getPropertyList()==null || other.getPropertyList().isEmpty())
				{
					return false;
				}
				else if(!ComparationUtils.areCollectionsEqual(this.getPropertyList(), other.getPropertyList()))
				{
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	@Override
	public int hashCode() {
		int prim = 29;
		return prim*prim + Integer.parseInt(this.getElementId());
	}
	/** 
	 * returns the id of the element 
	*/
	public String getElementId() {
		return elementId;
	}
	protected String getStringOrEmpty(String value)
	{
		if(value==null)
		{
			value = "";
		}
		return value;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public boolean hasProperties() {
		if(properties!=null && !properties.isEmpty() && properties.size() > 0)
		{
			return true;
		}
		return false;
	}
}
