package com.google.cloud.db;


public class GenericColumn implements IDatabaseColumn
{
	private boolean unique_column = false;
	private boolean primary_key = false;
	private String fieldName;
	private IDataType dataType;
	public GenericColumn(String fieldName,IDataType dataType)
	{
		this(false,false, fieldName, dataType);
	}
	public GenericColumn(boolean primaryKey, String fieldName, IDataType dataType)
	{
		this(primaryKey, false, fieldName, dataType);
	}
	public GenericColumn(boolean primarykey, boolean isUnique, String fieldName, IDataType dataType)
	{
		super();
		this.primary_key = primarykey;
		this.unique_column = isUnique;
		this.fieldName =fieldName;
		this.dataType = dataType;
	}
	@Override
	public boolean isPrimaryKey() {
		return primary_key;
	}
	@Override
	public String getFieldName() {
		return this.fieldName;
	}
	@Override
	public boolean isUnique() {
		return this.unique_column;
	}
	@Override
	public IDataType getDataType() {
		return this.dataType;
	}
	@Override
	public String toString() {
		return this.fieldName;
	}
}
