package com.google.cloud.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteDatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = SqliteDatabaseHelper.class
			.getSimpleName();
	public static final String DEFAULT_DATABASE_NAME = "CLOUD";

	public SqliteDatabaseHelper(Context context, String name) {
		super(context, name, null, TablesCreate.VERSION);
	}

	public SqliteDatabaseHelper(Context context) {
		this(context, DEFAULT_DATABASE_NAME);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createDatabase(db);
	}

	private void createDatabase(SQLiteDatabase db) {
		List<String> scripts = getCreatedScripts(db);
		for (String script : scripts) {
			db.execSQL(script);
		}
	}

	private List<String> getCreatedScripts(SQLiteDatabase db) {
		TablesCreate[] tables = TablesCreate.values();
		List<String> createdScripts = new ArrayList<String>();

		for (int k = 0; k < tables.length; k++) {
			String createScript = "";
			TablesCreate table = tables[k];
			List<String> primaryKeys = new ArrayList<String>();
			createScript += "CREATE TABLE IF NOT EXISTS "
					+ table.getTableName() + "( \n";
			createScript = createColumnDefintions(createScript, table,
					primaryKeys);
			createScript = createPrimaryKeyDefintions(createScript, table,
					primaryKeys);
			createScript += ");";
			createdScripts.add(createScript);
		}
		return createdScripts;
	}

	private String createPrimaryKeyDefintions(String createScript,
			TablesCreate table, List<String> primaryKeys) {
		if (!primaryKeys.isEmpty()) {
			createScript += ", PRIMARY KEY (";
			for (int k = 0; k < primaryKeys.size(); k++) {
				createScript += primaryKeys.get(k)
						+ (k == primaryKeys.size() - 1 ? "" : ", ");
			}
			createScript += ")\n";
		}
		return createScript;
	}

	private String createColumnDefintions(String createScript,
			TablesCreate table, List<String> primaryKeys) {
		GenericColumn[] cols = table.getColumns();
		for (int k = 0; k < cols.length; k++) {
			GenericColumn col = cols[k];
			createScript += col.getFieldName() + " "
					+ col.getDataType().getDataTypeName();
			if (col.isPrimaryKey() && col.isUnique()) {
				createScript += " PRIMARY KEY";
			} else if (col.isPrimaryKey()) {
				primaryKeys.add(col.getFieldName());
			} else if (col.isUnique()) {
				createScript += " UNIQUE";
			}
			createScript += (k == cols.length - 1 ? "" : ",") + "\n";
		}
		return createScript;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", destroying the old data");
		dropTables(db);
		createDatabase(db);

	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Downgrading database from version " + oldVersion + " to "
				+ newVersion + ", destroying the old data");
		dropTables(db);
		createDatabase(db);
	}

	private void dropTables(SQLiteDatabase db) {
		List<String> dropScripts = createDropScripts();
		for (String script : dropScripts) {
			db.execSQL(script);
		}
	}

	private List<String> createDropScripts() {
		TablesCreate[] tables = TablesCreate.values();
		List<String> dropScripts = new ArrayList<String>();
		for (int k = 0; k < tables.length; k++) {
			String dropScript = "";
			TablesCreate table = tables[k];
			dropScript += "DROP TABLE IF EXISTS " + table.getTableName() + ";";
			dropScripts.add(dropScript);
		}
		return dropScripts;
	}
}
