package com.google.cloud.db;

import com.google.cloud.db.objects.BasePropertyRow;
import com.google.cloud.db.objects.BaseElementRow;
import com.google.cloud.db.objects.ComputeRow;
import com.google.cloud.db.objects.DiskRow;
import com.google.cloud.db.objects.NicRow;
import com.google.cloud.db.objects.UserRow;

public enum TablesCreate {

	User(
			new GenericColumn(true, UserRow.COLUMN_USER_ID,SqlDataColumnType.String),
			new GenericColumn(UserRow.COLUMN_USER_NAME,	SqlDataColumnType.String),
			new GenericColumn(UserRow.COLUMN_USED_CPU, SqlDataColumnType.String),
			new GenericColumn(UserRow.COLUMN_USED_MEMORY,SqlDataColumnType.String), 
			new GenericColumn(UserRow.COLUMN_USED_NUM_VMS, SqlDataColumnType.String),
			new GenericColumn(UserRow.COLUMN_USED_STORAGE,SqlDataColumnType.String), 
			new GenericColumn(UserRow.COLUMN_QUOTA_CPU, SqlDataColumnType.String),
			new GenericColumn(UserRow.COLUMN_QUOTA_MEMORY,SqlDataColumnType.String), 
			new GenericColumn(UserRow.COLUMN_QUOTA_NUM_VMS, SqlDataColumnType.String),
			new GenericColumn(UserRow.COLUMN_QUOTA_STORAGE,SqlDataColumnType.String)),
			
BaseElement(new GenericColumn(
			true, BaseElementRow.COLUMN_NAME_ID, SqlDataColumnType.String),
			new GenericColumn(BaseElementRow.COLUMN_NAME_NAME,SqlDataColumnType.String), 
			new GenericColumn(BaseElementRow.COLUMN_NAME_TYPE, SqlDataColumnType.String)),
BaseProperty(
			new GenericColumn(BasePropertyRow.COLUMN_NAME_ID, SqlDataColumnType.String),
			new GenericColumn(BasePropertyRow.COLUMN_NAME_NAME, SqlDataColumnType.String),
			new GenericColumn(BasePropertyRow.COLUMN_NAME_TYPE,	SqlDataColumnType.String), 
			new GenericColumn(BasePropertyRow.COLUMN_NAME_CHANGE_ALLOWED, SqlDataColumnType.Boolean),
			new GenericColumn(BasePropertyRow.COLUMN_NAME_VISIBLE, SqlDataColumnType.Boolean)),
NicComponent(new GenericColumn(NicRow.COLUMN_NIC_UID, SqlDataColumnType.String),
			 new GenericColumn(NicRow.COLUMN_NIC_COMPUTEID, SqlDataColumnType.String),
			 new GenericColumn(NicRow.COLUMN_NIC_BASELEMENT_ID,SqlDataColumnType.String),
			 new GenericColumn(NicRow.COLUMN_NIC_MAC, SqlDataColumnType.String),
			 new GenericColumn(NicRow.COLUMN_NIC_IP, SqlDataColumnType.String)),
DiskComponent(new GenericColumn(true, DiskRow.COLUMN_DISK_UID,SqlDataColumnType.String),
			  new GenericColumn(DiskRow.COLUMN_DISK_COMPUTE_ID, SqlDataColumnType.String),
			  new GenericColumn(DiskRow.COLUMN_DISK_BASEELEMENT_ID, SqlDataColumnType.String),
			  new GenericColumn(DiskRow.COLUMN_DISK_TYPE, SqlDataColumnType.String),
			  new GenericColumn(DiskRow.COLUMN_DISK_TARGET, SqlDataColumnType.String)),
ComputeElement(
			new GenericColumn(true, ComputeRow.COLUMN_NAME_ID, SqlDataColumnType.String), 
			new GenericColumn(ComputeRow.COLUMN_NAME_TYPE, SqlDataColumnType.String),
			new GenericColumn(ComputeRow.COLUMN_NAME_NAME, SqlDataColumnType.String));
	
	public static final int VERSION = 1;
	private GenericColumn[] columns;

	private TablesCreate(GenericColumn... columns) {

		this.columns = columns;
	}

	public GenericColumn[] getColumns() {
		return columns;
	}

	public String getTableName() {
		return this.name();
	}
}
