package com.google.cloud.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.cloud.utils.constants.GeneralConstants;

public abstract class AbstractDatabaseRow implements IDatabaseRow {

	public AbstractDatabaseRow() {

	}
	public AbstractDatabaseRow(Cursor cursor) throws Exception
	{
		this.moveCursorToTheNextRow(cursor);
	}
	@Override
	public abstract TablesCreate getTable();

	@Override
	public void insert(SQLiteDatabase db) throws SQLException {
		if (db != null && db.isOpen() && !db.isReadOnly()) {
			internalInsert(db);// db.insert(table, nullColumnHack, values)
		} else {
			throw new SQLException(GeneralConstants.DATABASE_NULL_OR_CLOSED);
		}
	}

	private void internalInsert(SQLiteDatabase db) throws SQLException {
		long result = db.insertOrThrow(getTable().getTableName(), null,
				getContentValues());
		if (result < 0) {
			throw new SQLException(GeneralConstants.INSERTION_FAILED_MESSAGE);
		}
	}

	@Override
	public final void update(SQLiteDatabase db) throws SQLException {
		long result = db.update(getTable().getTableName(), getContentValues(),
				getWhereClause(), null);
		if (result < 0) {
			throw new SQLException(GeneralConstants.DATABASE_UPDATE_FAILED);
		}
	}

	@Override
	public void delete(SQLiteDatabase db) throws SQLException {
		if (db != null && db.isOpen() && !db.isReadOnly()) {
			deleteInternal(db);
		} else {
			throw new SQLException(GeneralConstants.DATABASE_NULL_OR_CLOSED);
		}
	}

	private void deleteInternal(SQLiteDatabase db) throws SQLException {
		long result = db.delete(getTable().getTableName(), getWhereClause(),
				null);
		if (result < 0) {
			throw new SQLException(GeneralConstants.DATABASE_DELETION_FAILED);
		}
	}

	@Override
	public void select(SQLiteDatabase db) throws SQLException {
		Cursor cursor = db.query(getTable().getTableName(), DatabaseAdapterUtils.getColumnNames(getTable()),
				getWhereClause(), null, null, null, null);
		moveCursorToTheNextRow(cursor);
		cursor.close();
	}
	
	private void moveCursorToTheNextRow(Cursor cursor) throws SQLException
	{
		if(cursor!=null && !cursor.isClosed())
		{
			if(cursor.isBeforeFirst())
			{
				cursor.moveToNext();
			}
			readDataFromCursor(cursor);
			cursor.moveToNext();
		}
		else
		{
			throw new SQLException(GeneralConstants.CURSOR_CLOSED);
		}
	}

	protected abstract ContentValues getContentValues() throws SQLException;
	protected abstract void readDataFromCursor(Cursor cursor) throws SQLException;
	protected abstract String getWhereClause();

}
