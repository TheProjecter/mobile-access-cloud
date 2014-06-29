package com.google.cloud.db.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.cloud.db.AbstractDatabaseRow;
import com.google.cloud.db.DatabaseAdapterUtils;
import com.google.cloud.db.TablesCreate;

public class UserRow extends AbstractDatabaseRow {
	public final static String COLUMN_USER_NAME = "username";
	public static final String COLUMN_USER_ID = "uid";
	public static final String COLUMN_USED_CPU = "usued_cpu";
	public static final String COLUMN_USED_MEMORY = "used_memory";
	public static final String COLUMN_USED_NUM_VMS = "used_num_vms";
	public static final String COLUMN_USED_STORAGE = "used_storage";
	public static final String COLUMN_QUOTA_CPU = "quota_cpu";
	public static final String COLUMN_QUOTA_MEMORY = "quota_memory";
	public static final String COLUMN_QUOTA_NUM_VMS = "quota_num_vms";
	public static final String COLUMN_QUOTA_STORAGE = "quota_storage";
	private String uid;
	private String name;
	private String used_cpu;
	private String used_num_vms;
	private String used_storage;
	private String used_memory;
	private String quota_cpu;
	private String quota_storage;
	private String quota_memory;
	private String quota_num_vms;

	public UserRow(String uid, String username, String used_cpu,
			String quota_cpu, String used_storage, String quota_storage,
			String used_memory, String quota_memory, String used_num_vms,
			String quota_num_vms) {
		this.uid = uid;
		this.name = username;
		this.used_cpu = used_cpu;
		this.quota_cpu = quota_cpu;
		this.used_memory = used_memory;
		this.quota_memory = quota_memory;
		this.used_num_vms = used_num_vms;
		this.quota_num_vms = quota_num_vms;
		this.used_storage = used_storage;
	}

	public String getUid() {
		return this.uid;
	}

	public String getUserName() {
		return this.name;
	}

	public String getUsedCpu() {
		return this.used_cpu;
	}

	public String getUsedMemory() {
		return this.used_memory;
	}

	public String getUsedNumVms() {
		return this.used_num_vms;
	}

	public String getUsedStorageSize() {
		return this.used_storage;
	}

	public String getQuotaCpu() {
		return this.quota_cpu;
	}

	public String getQuotaMemory() {
		return this.quota_memory;
	}

	public String getQuotaNumVms() {
		return this.quota_num_vms;
	}

	public String getQuotaStorageSize() {
		return this.quota_storage;
	}

	public TablesCreate getTable() {
		return TablesCreate.User;
	}

	@Override
	protected ContentValues getContentValues() throws SQLException {
		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_USER_ID, this.getUid());
		contentValues.put(COLUMN_USER_NAME, this.getUserName());
		contentValues.put(COLUMN_USED_CPU, this.getUsedCpu());
		contentValues.put(COLUMN_QUOTA_CPU, this.getQuotaCpu());
		contentValues.put(COLUMN_USED_MEMORY, this.getUsedMemory());
		contentValues.put(COLUMN_QUOTA_MEMORY, this.getUsedMemory());
		contentValues.put(COLUMN_QUOTA_NUM_VMS, this.getUsedNumVms());
		contentValues.put(COLUMN_USED_STORAGE, this.getUsedStorageSize());
		contentValues.put(COLUMN_QUOTA_STORAGE, this.getQuotaStorageSize());
		return contentValues;
	}

	@Override
	protected void readDataFromCursor(Cursor cursor) throws SQLException {
		readFromCursorInternal(cursor);

	}

	/**
	 * tries to read data from cursor.
	 * 
	 * @param cursor
	 * @throws Exception
	 */
	private void readFromCursorInternal(Cursor cursor) throws SQLException {
		int idxUserId = cursor.getColumnIndexOrThrow(COLUMN_USER_ID);
		if (!cursor.isNull(idxUserId)) {
			this.uid = cursor.getString(idxUserId);
		}
		int idxUserName = cursor.getColumnIndexOrThrow(COLUMN_USER_NAME);
		if (!cursor.isNull(idxUserName)) {
			this.name = cursor.getString(idxUserName);
		}

		int idxUsedCpu = cursor.getColumnIndexOrThrow(COLUMN_USED_CPU);
		if (!cursor.isNull(idxUsedCpu)) {
			this.used_cpu = cursor.getString(idxUsedCpu);
		}
		int idxQuotaCpu = cursor.getColumnIndexOrThrow(COLUMN_QUOTA_CPU);
		if (!cursor.isNull(idxUsedCpu)) {
			this.quota_cpu = cursor.getString(idxQuotaCpu);
		}
		int idxUsedMemory = cursor.getColumnIndexOrThrow(COLUMN_USED_MEMORY);
		if (!cursor.isNull(idxUsedMemory)) {
			this.used_memory = cursor.getString(idxUsedMemory);
		}
		int idxQuotaMemory = cursor.getColumnIndexOrThrow(COLUMN_QUOTA_MEMORY);
		if (!cursor.isNull(idxQuotaMemory)) {
			this.quota_memory = cursor.getString(idxQuotaMemory);
		}
		int idxUsedNum_vms = cursor.getColumnIndexOrThrow(COLUMN_USED_NUM_VMS);
		if (!cursor.isNull(idxUsedNum_vms)) {
			this.used_num_vms = cursor.getString(idxUsedNum_vms);
		}
		int idxQuotaNum_vms = cursor
				.getColumnIndexOrThrow(COLUMN_QUOTA_NUM_VMS);
		if (!cursor.isNull(idxQuotaNum_vms)) {
			this.quota_num_vms = cursor.getString(idxQuotaNum_vms);
		}
		int idxUsedStorageSize = cursor
				.getColumnIndexOrThrow(COLUMN_USED_STORAGE);
		if (!cursor.isNull(idxUsedStorageSize)) {
			this.used_storage = cursor.getString(idxUsedStorageSize);
		}
		int idxQuotaStorageSize = cursor
				.getColumnIndexOrThrow(COLUMN_QUOTA_STORAGE);
		if (!cursor.isNull(idxQuotaStorageSize)) {
			this.used_storage = cursor.getString(idxQuotaStorageSize);
		}
	}
	public Cursor find(SQLiteDatabase dbAdapter, Integer uid)
	{
		return dbAdapter.query(getTable().getTableName(), 
							   DatabaseAdapterUtils.getColumnNames(getTable()),
							   COLUMN_USER_ID + " = ?", new String[]{uid.toString()}, 
							   null, null, null);
	}
	@Override
	protected String getWhereClause() {
		String where = "";
		where += COLUMN_USER_ID + " = '" + this.getUid().toString() + "'";
		return where;
	}
}
