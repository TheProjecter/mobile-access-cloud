package com.google.cloud.settings;

import java.util.List;

import com.google.cloud.CloudKernelApplication;

public class Setting<T> implements ISetting<T>
{
	private String key;
	private SettingType type;
	private T defaultValue;
	private List<T> possibleValues;
	public Setting(String key, SettingType type, T defaultValue, List<T> possibleValues)
	{
		this.key = key;
		this.type = type;
		this.defaultValue = defaultValue;
		this.possibleValues = possibleValues;
	}
	@Override
	public T getDefaultValue()
	{
		return this.defaultValue;
	}
	@Override
	public void setDefaultValue(T value)
	{
		this.defaultValue = value;
	}
	@Override
	public String getKey()
	{
		return this.key;
	}
	@Override
	public SettingType getType()
	{
		return this.type;
	}
	@Override
	public List<T> getPossibleValues()
	{
		return this.possibleValues;
	}
	@Override
	@SuppressWarnings("unchecked")
	public T getValue() throws ClassCastException
	{
		ISettings settings = CloudKernelApplication.getInstance().getSettings();
		String value = settings.getSetting(key, String.valueOf(defaultValue));
		switch(type)
		{
		case BOOLEAN:
			return (T) Boolean.valueOf(value);
		case DOUBLE:
			return (T) Double.valueOf(value);
		case INTEGER:
			return (T) Integer.valueOf(value);
		case STRING:
			return (T) value;
		default:
			return null;
		}
	}
}
