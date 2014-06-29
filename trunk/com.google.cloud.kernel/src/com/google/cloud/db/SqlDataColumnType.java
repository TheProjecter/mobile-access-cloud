package com.google.cloud.db;

public enum SqlDataColumnType implements IDataType
{
	Boolean("BOOLEAN"),
	Integer("INTEGER"),
	Double("REAL"),
	String("TEXT"),
	Long("FLOAT");
	private String dataType;
	private SqlDataColumnType(String dataType)
	{
		this.dataType = dataType;
	}
	@Override
	public String getDataTypeName() {
		return this.dataType;
	}
	
}
