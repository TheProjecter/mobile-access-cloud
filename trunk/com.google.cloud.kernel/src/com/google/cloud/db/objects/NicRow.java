package com.google.cloud.db.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.cloud.db.AbstractDatabaseRow;
import com.google.cloud.db.DatabaseAdapterUtils;
import com.google.cloud.db.SqlDatabaseAdapter;
import com.google.cloud.db.TablesCreate;

public class NicRow extends AbstractDatabaseRow{
	public static final String COLUMN_NIC_UID = "uid";
	public static final String COLUMN_NIC_COMPUTEID = "computeid";
	public static final String COLUMN_NIC_IP = "nic_ip";
	public static final String COLUMN_NIC_MAC = "nic_mac";
	public static final String COLUMN_NIC_BASELEMENT_ID = "baseelement_id";
	private String nic_uid;
	private String compute_id;
	private String nic_ip;
	private String nic_mac;
	private String baseElementId;
	public NicRow(String nic_uid, 
				  String compute_id,
				  String baseElementId,  
				  String nic_ip,
				  String nic_mac)
	{
		this.nic_uid = nic_uid;
		this.compute_id = compute_id;
		this.nic_ip = nic_ip;
		this.nic_mac = nic_mac;
		this.baseElementId = baseElementId;
	}
	@Override
	public TablesCreate getTable() {
		return TablesCreate.NicComponent;
	}
	public NicRow(Cursor cursor) throws Exception
	{
		super(cursor);
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
	protected ContentValues getContentValues() throws SQLException {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NIC_UID, getNicUid());
		values.put(COLUMN_NIC_COMPUTEID, getNicComputeId());
		values.put(COLUMN_NIC_MAC, getNicMac());
		values.put(COLUMN_NIC_IP, getNicIp());
		values.put(COLUMN_NIC_BASELEMENT_ID, getBaseElementId());
		return values;
	}
	@Override
	protected void readDataFromCursor(Cursor cursor) throws SQLException {
		readInternalData(cursor);
		
	}
	private void readInternalData(Cursor cursor) throws SQLException
	{
		int idxNicUid = cursor.getColumnIndex(COLUMN_NIC_UID);
		this.nic_uid = getValue(cursor, idxNicUid);
		
		int idxNicComponentId = cursor.getColumnIndex(COLUMN_NIC_COMPUTEID);
		this.compute_id = getValue(cursor, idxNicComponentId);
		
		int idxBaseElementId = cursor.getColumnIndex(COLUMN_NIC_BASELEMENT_ID);
		this.baseElementId = getValue(cursor, idxBaseElementId);
		
		int idxNicMac = cursor.getColumnIndex(COLUMN_NIC_MAC);
		this.nic_mac = getValue(cursor, idxNicMac);
		
		int idxNicIp = cursor.getColumnIndex(COLUMN_NIC_IP);
		this.nic_ip = getValue(cursor, idxNicIp);
	}
	@Override
	protected String getWhereClause() {
		String where = "";
		where += COLUMN_NIC_COMPUTEID + " = '" + getNicComputeId() + "'";
		return where;
	}
	public String getNicUid()
	{
		return this.nic_uid;
	}
	public String getNicComputeId()
	{
		return this.compute_id;
	}
	public String getNicIp()
	{
		return this.nic_ip;
	}
	public String getNicMac()
	{
		return this.nic_mac;
	}
	public String getBaseElementId()
	{
		return this.baseElementId;
	}
	public static Cursor find(SQLiteDatabase db, String id)
	{
		TablesCreate table = TablesCreate.NicComponent;
		return db.query(table.getTableName(),DatabaseAdapterUtils.getColumnNames(table), COLUMN_NIC_COMPUTEID + " =?", new String[]{id}, null, null, null, null);
	}
}
