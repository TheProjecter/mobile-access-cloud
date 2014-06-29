package com.google.cloud.db;

import java.util.List;
import java.util.Map;

import com.google.cloud.db.objects.BasePropertyRow;
import com.google.cloud.db.objects.BaseElementRow;
import com.google.cloud.db.objects.ComputeRow;
import com.google.cloud.db.objects.DiskRow;
import com.google.cloud.db.objects.NicRow;

public interface IDatabaseAdapter 
{
	/** 
	 * close all the open connections
	 */
	public void closeAll();
	/**
	 * updates a list of rows
	 * @param rowsToUpdate the rows to be updated
	 * @return the rows that failed to be updated
	 */
	public List<IDatabaseRow> update(List<IDatabaseRow> rowsToUpdate);
	
	/**
	 * inserts a list of rows
	 * @param rowsToBeInserted the rows to be inserted
	 * @return the rows that didn't succeed
	 */
	public List<IDatabaseRow> insert(List<IDatabaseRow> rowsToBeInserted);
	/**
	 * deletes a list of rows from the databases
	 * @param rowsToBeDeleted the rows to be deleted
	 * @return the rows that weren't deleted
	 */
	public List<IDatabaseRow> delete(List<IDatabaseRow> rowsToBeDeleted);
	
	/**
	 *inserts a list of rows in a transactional way, meaning if one row didn't succeed rollback 
	 *everything.
	 *{@link http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html}
	 * #beginTransactionNonExclusive%28%29
	 * @param rowsToBeInserted the rows to be inserted
	 * @return true or false, regarding if the operation has succeeded or not
	 */
	public boolean  insertTransactional(List<IDatabaseRow> rowsToBeInserted);
	/**
	 * updates a list of rows in a transactional way, meaning if an error occurs 
	 * rollback should occur.
	 * {@link  http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html}
	 * @param rowsToBeUpdated the rows to be updated.
	 * @return true if everything goes well. false otherwise
	 */
	public boolean updateTransactional(List<IDatabaseRow> rowsToBeUpdated);
	/**
	 * deletes a row from the database
	 * @param rowToBeDeleted the row to be deleted
	 * @return true if the operation succeeds, false otherwise
	 */
	public boolean delete(IDatabaseRow rowToBeDeleted);
	/**
	 * inserts a row in to the database
	 * @param rowToBeInserted the row to be inserted.
	 * @return true if the operation succeeds, false otherwise.
	 */
	public boolean insert(IDatabaseRow rowToBeInserted);
	/**
	 * updates a row in to the database.
	 * @param rowToBeUpdated the row to be updated.
	 * @return true if the operation succeeds, false otherwise.
	 */
	public boolean update(IDatabaseRow rowToBeUpdated);
	/**
	 * deletes a list of rows in a transactional way
	 * @param rowsToBeDeleted the list of rows to be deleted
	 * @return true if it succeeded or false if not
	 */
	public boolean deleteTransactional(List<IDatabaseRow> rowsToBeDeleted);
	/** 
	 * deletes all the tables from the db.
	 */
	public void deleteAll();
	/**
	 * gets a Network element from the db by its id.
	 * @param elementId the id of the Network.
	 * @return the Network object.
	 */
	public BaseElementRow getBaseElementRowById(String elementId);
	/**
	 * gets a map of properties by id. the map has as keys the id and as values the 
	 * database row.
	 * @param uid the id of the object for which the properties have to be loaded.
	 * @return a list of properties
	 */
	public Map<String, List<BasePropertyRow>> getBasePropertiesById(String uid);
	/**
	 * retrieves a database row property by its id.
	 * @param uid the id of the property row to be retrieved.
	 * @return a BasePropertyRow that has the id uid.
	 */
	public IDatabaseRow getBasePropertyById(String uid);
	/**
	 * retrieves a compute base on its id.
	 * @param elementId the id of the compute object to be retrieved.
	 * @return a computerow object with the id elementId.
	 */
	public ComputeRow getComputeRowById(String elementId);
	public Map<String, List<NicRow>> getNicById(String id);
	public Map<String, List<DiskRow>> getDiskById(String id);
}
