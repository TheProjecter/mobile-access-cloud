package com.google.cloud.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.db.objects.BaseElementRow;
import com.google.cloud.db.objects.BasePropertyRow;
import com.google.cloud.db.objects.ComputeRow;
import com.google.cloud.db.objects.DiskRow;
import com.google.cloud.db.objects.NicRow;
import com.google.cloud.utils.constants.GeneralConstants;

public class SqlDatabaseAdapter implements IDatabaseAdapter {

	private static final String TAG = SqlDatabaseAdapter.class.getSimpleName();
	private Object dbLock = new Object();
	private Map<String, SqliteDatabaseHelper> databaseHelpers = new HashMap<String, SqliteDatabaseHelper>();
	private Map<String, SQLiteDatabase> databases = new HashMap<String, SQLiteDatabase>();
	private static IDatabaseAdapter instance = null;
	private boolean isDatabaseOpened = false;

	protected SqlDatabaseAdapter() {

	}

	private boolean getIsDatabaseOpened() {
		if (!isDatabaseOpened) {
			openAccess(true);
		}
		return isDatabaseOpened;
	}

	public static IDatabaseAdapter getInstance() {
		if (instance == null) {
			instance = new SqlDatabaseAdapter();
		}
		return instance;
	}

	protected void openAccess(boolean write) throws SQLException {
		synchronized (dbLock) {
			String dbName = GeneralConstants.DEFAULT_DATABASE_NAME;
			Context context = CloudKernelApplication.getInstance();
			SqliteDatabaseHelper helper = null;
			if (!databaseHelpers.containsKey(dbName)) {
				helper = new SqliteDatabaseHelper(context);
				databaseHelpers.put(dbName, helper);
			} else {
				helper = databaseHelpers.get(dbName);
			}
			SQLiteDatabase db = databases.get(dbName);
			if (write) {
				if (db != null && db.isReadOnly()) {
					db.close();
					databases.remove(db);
					db = null;
				}
			}
			if (db == null) {
				if (write) {
					db = helper.getWritableDatabase();
				} else {
					db = helper.getReadableDatabase();
				}
				databases.put(dbName, db);
				if (db.isOpen()) {
					isDatabaseOpened = true;
				}
			}
		}
	}

	@Override
	public void closeAll() {
		synchronized (dbLock) {
			Set<String> dbHelperKeys = databaseHelpers.keySet();
			for (String key : dbHelperKeys) {
				SqliteDatabaseHelper helper = databaseHelpers.get(key);
				if (helper != null) {
					helper.close();
					databases.remove(helper);
				}
			}
		}

	}

	@Override
	public List<IDatabaseRow> update(List<IDatabaseRow> rowsToUpdate) {
		List<IDatabaseRow> failedRows = new ArrayList<IDatabaseRow>();
		synchronized (dbLock) {
			getIsDatabaseOpened();
			for (IDatabaseRow row : rowsToUpdate) {
				try {
					row.update(getDB());
				} catch (Exception e) {
					Log.e(TAG, GeneralConstants.UPDATE_FAILED_MESSAGE,e);
					failedRows.add(row);
				}
			}
		}
		return failedRows;
	}

	private SQLiteDatabase getDB() {
		return databases.get(GeneralConstants.DEFAULT_DATABASE_NAME);
	}

	@Override
	public List<IDatabaseRow> insert(List<IDatabaseRow> rowsToBeInserted) {
		List<IDatabaseRow> failedRows = new ArrayList<IDatabaseRow>();
		synchronized (dbLock) {
			getIsDatabaseOpened();
			for (IDatabaseRow row : rowsToBeInserted) {
				try {
					row.insert(getDB());
				} catch (Exception e) {
					Log.e(TAG, GeneralConstants.INSERTION_FAILED_MESSAGE,e);
					failedRows.add(row);
				}
			}
		}
		return failedRows;
	}

	@Override
	public List<IDatabaseRow> delete(List<IDatabaseRow> rowsToBeDeleted) {
		List<IDatabaseRow> failedDeleted = new ArrayList<IDatabaseRow>();
		synchronized (dbLock) {
			getIsDatabaseOpened();
			for (IDatabaseRow row : rowsToBeDeleted) {
				try {
					row.delete(getDB());
				} catch (Exception e) {
					Log.e(TAG, GeneralConstants.DATABASE_DELETION_FAILED ,e);
					failedDeleted.add(row);
				}
			}
		}
		return failedDeleted;
	}

	@Override
	public boolean insertTransactional(List<IDatabaseRow> rowsToBeInserted) {
		boolean success = false;
		synchronized (dbLock) {
			getIsDatabaseOpened();
			getDB().beginTransaction();
			try {
				for (IDatabaseRow row : rowsToBeInserted) {
					row.insert(getDB());
				}
				success = true;
				getDB().setTransactionSuccessful();
			} catch (Exception e) {
				Log.e(TAG, GeneralConstants.INSERTION_TRANSACTIONAL_FAILED,e);
			}
			getDB().endTransaction();

		}
		return success;
	}
	@Override
	public ComputeRow getComputeRowById(String elementId) {
		ComputeRow computeRow = null;
		synchronized (dbLock) {
			getIsDatabaseOpened();
			try 
			{
				if(elementId!=null)
				{
					computeRow = new ComputeRow(elementId, null, null);
					computeRow.select(getDB());
				}
			} catch (Exception e) {
				Log.e(TAG, "Exception in querying for the Compute.Compute with id : " + elementId + "doesn't exist.", e);
				computeRow = null;
			}
		}
		return computeRow;
	}
	@Override
	public BaseElementRow getBaseElementRowById(String elementId)
	{
		BaseElementRow row = null;
		synchronized (dbLock) {
			getIsDatabaseOpened();
			try 
			{
				if(elementId!=null)
				{
					row = new BaseElementRow(elementId,null, null);
					row.select(getDB());
				}
			} catch (Exception e) {
				Log.e(TAG, "Exception finding the object with id : " + elementId, e);
				row = null;
			}
		}
		return row;
	}
	@Override
	public boolean updateTransactional(List<IDatabaseRow> rowsToBeUpdated) {
		boolean success = false;
		synchronized (dbLock) {
			getIsDatabaseOpened();
			getDB().beginTransaction();
			try {
				for (IDatabaseRow row : rowsToBeUpdated) {
					row.update(getDB());
				}
				success = true;
				getDB().setTransactionSuccessful();
			} catch (Exception e) {
				Log.e(TAG, GeneralConstants.INSERTION_TRANSACTIONAL_FAILED, e);
			}
			getDB().endTransaction();

		}
		return success;
	}

	@Override
	public boolean deleteTransactional(List<IDatabaseRow> rowsToBeDeleted) {
		boolean success = false;
		synchronized (dbLock) {
			getIsDatabaseOpened();
			getDB().beginTransaction();
			try {
				for (IDatabaseRow row : rowsToBeDeleted) {
					row.delete(getDB());
				}
				success = true;
			} catch (Exception e) {
				Log.e(TAG, GeneralConstants.DELETION_TRANSACTIONAL_FAILED, e);
			}
			getDB().endTransaction();

		}
		return success;
	}

	@Override
	public boolean delete(IDatabaseRow rowToBeDeleted) {
		boolean success = false;
		synchronized (dbLock) {
			getIsDatabaseOpened();
			try {
				rowToBeDeleted.delete(getDB());
				success = true;
			} catch (Exception e) {
				Log.e(TAG, GeneralConstants.DELETION_TRANSACTIONAL_FAILED ,e);
			}
		}
		return success;
	}

	@Override
	public boolean insert(IDatabaseRow rowToBeInserted) {
		boolean success = false;
		synchronized (dbLock) {
			getIsDatabaseOpened();
			try {
				rowToBeInserted.insert(getDB());
				success = true;
			} catch (Exception e) {
				Log.e(TAG, GeneralConstants.INSERTION_FAILED_MESSAGE, e);
			}
		}
		return success;
	}

	@Override
	public boolean update(IDatabaseRow rowToBeUpdated) {
		boolean success = false;
		synchronized (dbLock) {
			getIsDatabaseOpened();
			try {
				rowToBeUpdated.update(getDB());
				success = true;
			} catch (Exception e) {
				Log.e(TAG, GeneralConstants.INSERTION_FAILED_MESSAGE, e);
			}
		}
		return success;
	}

	@Override
	public void deleteAll() {
		synchronized (dbLock) {
			openAccess(true);
			try {
				clearAllTables(getDB());
			} catch (Exception e) {
				Log.e(TAG, "Error occured while deleting data.", e);
			}
		}
	}

	private void clearTable(SQLiteDatabase db, TablesCreate table) {
		db.delete(table.getTableName(), null, null);
	}

	public void clearAllTables(SQLiteDatabase db) {
		for (TablesCreate table : TablesCreate.values()) {
			clearTable(db, table);
		}
	}

	@Override
	public IDatabaseRow getBasePropertyById(String uid) {
		IDatabaseRow row = null;
		synchronized (dbLock) {
		 	getIsDatabaseOpened();
		 	try {
				row = new BasePropertyRow(uid, null, null, null, null);
				row.insert(getDB());
			} catch (Exception e) {
				Log.e(TAG, "Exception in getting the properies." , e);
				row = null;
			}
		}
		return row;
	}
	@Override
	public Map<String, List<DiskRow>> getDiskById(String id)
	{
		Map<String, List<DiskRow>> diskMap = new HashMap<String, List<DiskRow>>();
		List<DiskRow> diskList = new ArrayList<DiskRow>();
		Cursor cursor = null;
		synchronized (dbLock) {
			getIsDatabaseOpened();
			try
			{
				try 
				{
					cursor = DiskRow.find(getDB(), id);
					while(!cursor.isAfterLast())
					{
						DiskRow diskRow = new DiskRow(cursor);
						diskList.add(diskRow);
					}
					diskMap.put(id, diskList);
				} catch (Exception e) {
					Log.e(TAG, "retrieving disk by id." + id, e);
					diskMap = null;
				}
			}
			finally
			{
				if(cursor!=null)
				{
					cursor.close();
				}
			}
		}
		return diskMap;
	}
	@Override
	public Map<String, List<NicRow>> getNicById(String id)
	{
		Map<String, List<NicRow>> nicMap = new HashMap<String, List<NicRow>>();
		List<NicRow> nicList = new ArrayList<NicRow>();
		Cursor cursor = null;
		synchronized (dbLock) {
			try 
			{
				try 
				{
					cursor = NicRow.find(getDB(), id);
					while(!cursor.isAfterLast())
					{
						NicRow nic = new NicRow(cursor);
						nicList.add(nic);
					}
					nicMap.put(id, nicList);
				} catch (Exception e) {
					Log.e(TAG, "the nic row object with the id : " + id + " was not found", e);
					nicMap = null;
				}
			}finally
			{
				if(cursor!=null)
				{
					cursor.close();
				}
			}
		}
		return nicMap;
	}
	@Override
	public Map<String, List<BasePropertyRow>> getBasePropertiesById(String uid) {
		Map<String, List<BasePropertyRow>> rows = new HashMap<String, List<BasePropertyRow>>();
		List<BasePropertyRow> listOfProperties = new ArrayList<BasePropertyRow>();
		Cursor cursor = null;
		synchronized (dbLock) {
			getIsDatabaseOpened();
			try 
			{	
				cursor = BasePropertyRow.find(getDB(), uid);
				while(!cursor.isAfterLast())
				{
					try
					{
						BasePropertyRow row = new BasePropertyRow(cursor);
						listOfProperties.add(row);
					}
					catch (Exception e) {
						Log.e(TAG, "exception in reading the rows" ,e);
						rows = null;
					}
				}
				rows.put(uid, listOfProperties);
			} finally{
				if(cursor!=null && !cursor.isClosed())
				{
					cursor.close();
				}
			}
		}
		return rows;
	}
}