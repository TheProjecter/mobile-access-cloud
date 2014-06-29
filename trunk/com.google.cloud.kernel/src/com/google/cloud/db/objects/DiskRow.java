package com.google.cloud.db.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.cloud.db.AbstractDatabaseRow;
import com.google.cloud.db.DatabaseAdapterUtils;
import com.google.cloud.db.TablesCreate;

public class DiskRow extends AbstractDatabaseRow
{
	public static final String COLUMN_DISK_UID = "uid";
	public static final String COLUMN_DISK_TYPE = "disk_type";
	public static final String COLUMN_DISK_TARGET = "disk_target";
	public static final String COLUMN_DISK_COMPUTE_ID = "compute_id";
	public static final String COLUMN_DISK_BASEELEMENT_ID = "baseelement_id";
	private String uid;
	private String type;
	private String target;
	private String computeId;
	private String baseElementId;
	public DiskRow(String uid,
				   String computeId, 
				   String baseElementId,
				   String type, 
				   String target)
	{
		this.uid = uid;
		this.computeId = computeId;
		this.baseElementId = baseElementId;
		this.type = type;
		this.target = target;
	}
	public DiskRow(Cursor cursor) throws Exception
	{
		super(cursor);
	}
	@Override
	public TablesCreate getTable() {
		return TablesCreate.DiskComponent;
	}

	public String getUid() {
		return uid;
	}
	public String getType() {
		return type;
	}
	public String getTarget() {
		return target;
	}
	public String getComputeId() {
		return computeId;
	}
	public String getBaseElementId()
	{
		return this.baseElementId;
	}
	@Override
	protected ContentValues getContentValues() throws SQLException {
		ContentValues values = new ContentValues();
		values.put(COLUMN_DISK_UID, getUid());
		values.put(COLUMN_DISK_TYPE, getType());
		values.put(COLUMN_DISK_TARGET, getTarget());
		values.put(COLUMN_DISK_COMPUTE_ID, getComputeId());
		values.put(COLUMN_DISK_BASEELEMENT_ID, getBaseElementId());
		return values;
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
	private void readInternalData(Cursor cursor) throws SQLException
	{
		int idxDiskUid = cursor.getColumnIndex(COLUMN_DISK_UID);
		this.uid = getValue(cursor, idxDiskUid);
		
		int idxDiskComputeId = cursor.getColumnIndex(COLUMN_DISK_COMPUTE_ID);
		this.computeId = getValue(cursor, idxDiskComputeId);
		
		int idxDiskType = cursor.getColumnIndex(COLUMN_DISK_TYPE);
		this.type = getValue(cursor, idxDiskType);
		
		int idxDiskTarget = cursor.getColumnIndex(COLUMN_DISK_TARGET);
		this.target = getValue(cursor, idxDiskTarget);
	}
	@Override
	protected String getWhereClause() {
		String where = "";
		where += COLUMN_DISK_COMPUTE_ID + " = '" + getComputeId() + "'";
		return where;
	}
	public static Cursor find(SQLiteDatabase db, String uid)
	{
		TablesCreate table = TablesCreate.DiskComponent;
		//String whereClause = "" + COLUMN_NAME_NETWORKID + " ='" + uid + "'"; 
		return db.query(table.getTableName(), 
						DatabaseAdapterUtils.getColumnNames(table), 
						COLUMN_DISK_COMPUTE_ID + " =?", new String[]{uid}, null, null, null);
	}

}
