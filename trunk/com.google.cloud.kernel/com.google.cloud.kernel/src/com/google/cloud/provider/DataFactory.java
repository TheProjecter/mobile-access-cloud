package com.google.cloud.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.client.objects.AttachedNetwork;
import com.google.cloud.client.objects.AttachedStorage;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.User;
import com.google.cloud.db.DatabaseAdapterUtils;
import com.google.cloud.db.IDatabaseAdapter;
import com.google.cloud.db.IDatabaseRow;
import com.google.cloud.db.objects.BaseElementRow;
import com.google.cloud.db.objects.BasePropertyRow;
import com.google.cloud.db.objects.ComputeRow;
import com.google.cloud.db.objects.DiskRow;
import com.google.cloud.db.objects.NicRow;

public class DataFactory implements IDataFactory {

	private static final String TAG = DataFactory.class.getSimpleName();
	private static IDataFactory instance = null;
	private IDatabaseAdapter dbAdapter = null;

	protected DataFactory() {
		dbAdapter = CloudKernelApplication.getInstance().getDatabaseAdapter();
	}

	public static IDataFactory getInstance() {
		if (instance == null) {
			instance = new DataFactory();
		}
		return instance;
	}

	@Override
	public void deleteAllNetworks() {

	}

	@Override
	public void insertElements(List<BaseElement> baseElementList) {
		List<IDatabaseRow> rows = new ArrayList<IDatabaseRow>();
		for (BaseElement baseElement : baseElementList) {
			List<IDatabaseRow> dbrows = DatabaseAdapterUtils
					.getDatabaseRows(baseElement);
			rows.addAll(dbrows);
			// row = new NetworkRow(n, name, group, size, address, isPublic,
			// userId)
		}
		getAdapter().insertTransactional(rows);
	}


	@Override
	public void updateElement(BaseElement element) {
		List<IDatabaseRow> rows = null;
		rows = DatabaseAdapterUtils.getDatabaseRows(element);
		getAdapter().updateTransactional(rows);
	}

	@Override
	public void insertElement(BaseElement element) {
		List<IDatabaseRow> rows = null;
		rows = DatabaseAdapterUtils.getDatabaseRows(element);
		getAdapter().insert(rows);
	}

	public IDatabaseAdapter getAdapter() {
		return CloudKernelApplication.getInstance().getDatabaseAdapter();
	}


	@Override
	public void insertUser(User user) {
		
	}

	@Override
	public void insertUsers(List<User> users) {
		List<IDatabaseRow> rows = new ArrayList<IDatabaseRow>();
		
		getAdapter().insertTransactional(rows);

	}

	@Override
	public void deleteUser(User user) {

	}

	@Override
	public void deleteUsers(List<User> users) {
	
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUsers(List<User> users) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		Log.d(TAG, "deletes all teh data from the database.");
		getAdapter().deleteAll();
	}

	@Override
	public boolean deleteBaseElementById(String elementId) {
		BaseElementRow row = getAdapter().getBaseElementRowById(elementId);
		return getAdapter().delete(row);
	}


	@Override
	public void deleteComputeById(String elementId) {
		Log.d(TAG, "deleting a compute based on its id");
	}

	@Override
	public Compute getComputeById(String elementId) {
		Log.d(TAG, "get a compute by id");
		Compute compute = null;
		ComputeRow computeRow = getAdapter().getComputeRowById(elementId);
		if(computeRow!=null)
		{
			Map<String , List<BasePropertyRow>> propRows = getAdapter().getBasePropertiesById(elementId);
			Map<String, List<NicRow>> nicRows = getAdapter().getNicById(elementId);
			Map<String, List<DiskRow>> diskRows = getAdapter().getDiskById(elementId);
			compute = DatabaseAdapterUtils.convertComputeRowToCompute(computeRow, propRows, nicRows, diskRows);
		}
		return compute;
	}
	@Override
	public BaseElement getElementById(String elementId) {
		BaseElement baseElement = null;
		Log.d(TAG, "gets an element by id." + elementId);
		//we check to see if the owner of the prop exists.
		//if it doesn't then there is no point to search for its properties
		BaseElementRow row = getAdapter().getBaseElementRowById(elementId);
		if (row != null) {
			Map<String, List<BasePropertyRow>> propRows = getAdapter().getBasePropertiesById(elementId);
			baseElement = DatabaseAdapterUtils.convertElementRowToElement(row, propRows);
		}
		return baseElement;
	}

	@Override
	public void insertCompute(Compute compute) {
		Log.d(TAG, "insert a compute into the database");
		List<IDatabaseRow> rowsToInsert = DatabaseAdapterUtils.getDatabaseRows(compute);
		getAdapter().insertTransactional(rowsToInsert);
	}
}
