package com.google.cloud.db;

public interface IDatabaseColumn {
	/**
	 * checks if a column is a primary
	 * @return true or false
	 */
	public boolean isPrimaryKey();
	/**
	 * the name of column field we are trying to modify.
	 * @return the name of the field
	 */
	public String getFieldName();
	/**
	 * checks if a column is unique or not
	 * @return true or false.
	 */
	public boolean isUnique();
	/**
	 * checks the datatype of the column
	 * @return the data type of the column. e.g Integer, double, string, etc.
	 */
	public IDataType getDataType();
}
