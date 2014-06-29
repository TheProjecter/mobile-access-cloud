package com.google.cloud.db.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.google.cloud.db.AbstractDatabaseRow;
import com.google.cloud.db.TablesCreate;

public class BaseElementRow extends AbstractDatabaseRow{

	public static final String COLUMN_NAME_ID = "uid";
	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_NAME_TYPE = "object_type";
	private String baseElementId;
	private String baseElementName;
	private String objectType;
	@Override
	public TablesCreate getTable() {
		return TablesCreate.BaseElement;
	}
	public BaseElementRow(String baseElementId,String name, String baseType)
	{
		this.baseElementId = baseElementId;
		this.objectType = baseType;
		this.baseElementName = name;
	}
	@Override
	protected ContentValues getContentValues() throws SQLException {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, getBaseElementId());
		values.put(COLUMN_NAME_NAME, getName());
		values.put(COLUMN_NAME_TYPE, getObjectType());
		 return values;
	}
	
	@Override
	protected void readDataFromCursor(Cursor cursor) throws SQLException {
		readInternalDataFromCursor(cursor);
		
	}
	private String getValue(Cursor cursor, int columnIndex)
	{
		if(cursor.isNull(columnIndex))
		{
			return null;
		}
		else 
			return cursor.getString(columnIndex);
	}
	private void readInternalDataFromCursor(Cursor cursor) throws SQLException
	{
		int idxBaseElementId = cursor.getColumnIndex(COLUMN_NAME_ID);
		this.baseElementId = getValue(cursor, idxBaseElementId);
		
		int idxBaseElementName = cursor.getColumnIndex(COLUMN_NAME_NAME);
		this.baseElementName = getValue(cursor, idxBaseElementName);
		
		int idxBaseElementType = cursor.getColumnIndex(COLUMN_NAME_TYPE);
		this.objectType = getValue(cursor, idxBaseElementType);
	}
	@Override
	protected String getWhereClause() {
		String where = "";
		where += COLUMN_NAME_ID + " = '" + getBaseElementId() + "'";
		return where;
	}
	public String getName()
	{
		return this.baseElementName;
	}
	public String getBaseElementId()
	{
		return this.baseElementId;
	}
	public String getObjectType()
	{
		return this.objectType;
	}
}
