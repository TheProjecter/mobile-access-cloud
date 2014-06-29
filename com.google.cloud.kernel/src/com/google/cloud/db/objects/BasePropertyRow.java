package com.google.cloud.db.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.cloud.db.AbstractDatabaseRow;
import com.google.cloud.db.DatabaseAdapterUtils;
import com.google.cloud.db.TablesCreate;

public class BasePropertyRow extends AbstractDatabaseRow {
	public static final String COLUMN_NAME_ID = "elementId";
	public static final String COLUMN_NAME_NAME = "propertyname";
	public static final String COLUMN_NAME_TYPE = "propertytype";
	public static final String COLUMN_NAME_CHANGE_ALLOWED = "propertychange";
	public static final String	COLUMN_NAME_VISIBLE = "ispropertyvisible";
	private String elementId;
	private String propertyName;
	private String propertyType;
	private Boolean isChangeAllowed;
	private Boolean isVisible;
	public BasePropertyRow(String elementId,
						   String propertyType, 
						   String propertyName,
						   Boolean isChangeAllowed, 
						   Boolean isVisible)
	{
		this.elementId = elementId;
		this.propertyType = propertyType;
		this.propertyName = propertyName;
		this.isChangeAllowed = isChangeAllowed;
		this.isVisible = isVisible;
	}
	public BasePropertyRow(Cursor cursor) throws Exception
	{
		super(cursor);
	}
	@Override
	public TablesCreate getTable() {
		return TablesCreate.BaseProperty;
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
	private Boolean getBoolValue(Cursor cursor, int columnIndex)
	{
		if(cursor.isNull(columnIndex))
		{
			return null;
		}
		else 
		{
			int intIndex = cursor.getInt(columnIndex);
			if(intIndex <= 0)
			{
				return Boolean.FALSE;
			}
			else
				return Boolean.TRUE;
		}
	}
	@Override
	protected ContentValues getContentValues() throws SQLException {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, getElementId());
		values.put(COLUMN_NAME_NAME, getPropertyName());
		values.put(COLUMN_NAME_TYPE, getPropertyType());
		values.put(COLUMN_NAME_CHANGE_ALLOWED, isChangeAllowed());
		values.put(COLUMN_NAME_VISIBLE, getIsVisible());
		return values;
	}
	@Override
	protected void readDataFromCursor(Cursor cursor) throws SQLException {
		int idxBaseElementId = cursor.getColumnIndex(COLUMN_NAME_ID);
		this.elementId = getValue(cursor, idxBaseElementId);
		
		int idxPropertyType = cursor.getColumnIndex(COLUMN_NAME_TYPE);
		this.propertyType = getValue(cursor, idxPropertyType);
		
		int idxPropertyName = cursor.getColumnIndex(COLUMN_NAME_NAME);
		this.propertyName = getValue(cursor, idxPropertyName);
		
		int idxPropertyChangeAllowed = cursor.getColumnIndex(COLUMN_NAME_CHANGE_ALLOWED);
		this.isChangeAllowed = getBoolValue(cursor, idxPropertyChangeAllowed);
		
		int idxPropertyIsVisible = cursor.getColumnIndex(COLUMN_NAME_VISIBLE);
		this.isVisible = getBoolValue(cursor, idxPropertyIsVisible);
	}
	@Override
	protected String getWhereClause() {
		String where = "";
		where +="" + COLUMN_NAME_ID + " ='" + getElementId() + "'";
		return where;
	}
	public String getElementId()
	{
		return this.elementId;
	}
	public String getPropertyName()
	{
		return this.propertyName;
	}
	public String getPropertyType()
	{
		return this.propertyType;
	}
	public Boolean isChangeAllowed()
	{
		return this.isChangeAllowed;
	}
	public Boolean getIsVisible()
	{
		return isVisible;
	}
	public static Cursor find(SQLiteDatabase db, String uid)
	{
		TablesCreate table = TablesCreate.BaseProperty;
		//String whereClause = "" + COLUMN_NAME_NETWORKID + " ='" + uid + "'"; 
		return db.query(table.getTableName(), DatabaseAdapterUtils.getColumnNames(table), COLUMN_NAME_ID + " =?", new String[]{uid}, null, null, null);
	}
}
