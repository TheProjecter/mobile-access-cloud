package com.google.cloud.db;

import android.database.sqlite.SQLiteDatabase;

public interface IDatabaseRow {

	public TablesCreate getTable();
	
	public void insert(SQLiteDatabase db) throws Exception;

	public void update(SQLiteDatabase db) throws Exception;
	
	public void delete(SQLiteDatabase db) throws Exception;
	
	public void select(SQLiteDatabase db) throws Exception;

}