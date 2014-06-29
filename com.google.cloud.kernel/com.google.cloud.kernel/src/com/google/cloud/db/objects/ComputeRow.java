package com.google.cloud.db.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.cloud.db.AbstractDatabaseRow;
import com.google.cloud.db.DatabaseAdapterUtils;
import com.google.cloud.db.TablesCreate;

public class ComputeRow extends AbstractDatabaseRow{
	public static final String COLUMN_NAME_ID= "uid";
	public static final String COLUMN_NAME_NAME	="name";
	public static final String COLUMN_NAME_TYPE = "object_type";
	private String id;
	private String name;
	private String objectType;
	@Override
	public TablesCreate getTable() {
		return TablesCreate.ComputeElement;
	}
	public ComputeRow(Cursor cursor) throws Exception
	{
		super(cursor);
	}
	public ComputeRow(String id, String objectType,String name)
	{
		this.id = id;
		this.name = name;
		this.objectType = objectType;
	}
	@Override
	protected ContentValues getContentValues() throws SQLException {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, getId());
		values.put(COLUMN_NAME_NAME, getName());
		values.put(COLUMN_NAME_TYPE, getObjectType());
		return values;
	}
	public String getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public String getObjectType()
	{
		return this.objectType;
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
	@Override
	protected void readDataFromCursor(Cursor cursor) throws SQLException {
		readInternalData(cursor);
	}
	private void readInternalData(Cursor cursor)
	{
		int idxComputeId = cursor.getColumnIndex(COLUMN_NAME_ID);
		this.id = getValue(cursor, idxComputeId);
		
		int idxComputeName = cursor.getColumnIndex(COLUMN_NAME_NAME);
		this.name = getValue(cursor, idxComputeName);
		
		int idxBaseType = cursor.getColumnIndex(COLUMN_NAME_TYPE);
		this.objectType = getValue(cursor, idxBaseType);
	}

	@Override
	protected String getWhereClause() {
		String where = "";
		where += COLUMN_NAME_ID + " = '" + getId() + "'";
		return where;
	}
	public static Cursor find(SQLiteDatabase db, String uid)
	{
		TablesCreate table = TablesCreate.ComputeElement;
		return db.query(table.getTableName(), DatabaseAdapterUtils.getColumnNames(table), COLUMN_NAME_ID + " =?", new String[]{uid}, null, null,null);
	}
}
