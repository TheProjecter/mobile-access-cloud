package com.google.cloud.ui.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.activities.CloudActivity;
import com.google.cloud.ui.R;
import com.google.cloud.ui.adapters.StartMenuAdapter;
import com.google.cloud.utils.ItemValuePair;
import com.google.cloud.utils.constants.GeneralConstants;

public class StartMenu extends CloudActivity {
	//Texts shown in ListView
	List<ItemValuePair> operationList;
	private StartMenuAdapter adapter;
	private static final String TAG = StartMenu.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
		initializeData();
		initializeAdapter();
		//startInitialUpdate();
	}
	/**
	 * starts initial update.it tries to update the entire collection from the server
	 * or if the parameter flag that the method updateCollections takes is false, it just
	 * loads the data from the database.
	 *
	 */
	private void startInitialUpdate()
	{
		CloudKernelApplication.getInstance().getDataProvider().getAllColectionsFromServer(true);
	}
	private void initializeData()
	{
		operationList = new ArrayList<ItemValuePair>();
		ItemValuePair networkItem = new ItemValuePair(GeneralConstants.NETWORK_COLLECTION, "Show Networks");
		operationList.add(networkItem);
		ItemValuePair storageItem = new ItemValuePair(GeneralConstants.STORAGE_COLLECTION, "Show Storages");
		operationList.add(storageItem);
		ItemValuePair computeItem = new ItemValuePair(GeneralConstants.COMPUTE_COLLECTION, "Show Computes");
		operationList.add(computeItem);
		ItemValuePair userItem = new ItemValuePair(GeneralConstants.USER_COLLECTION, "Show users");
		operationList.add(userItem);
	}
	private void initializeAdapter()
	{
		adapter = new StartMenuAdapter(this, operationList);
		ListView startListView = (ListView)this.findViewById(R.id.lv_startmenu_list);
		startListView.setAdapter(adapter);
		TextView emptyTextview = (TextView)this.findViewById(android.R.id.empty);
		emptyTextview.setText("No Content Available");
		updateEmptyViewVisibility(startListView, emptyTextview);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base_elements_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) 
		{
			case R.id.force_update:
				startInitialUpdate();
				return true;
			default:	
				return super.onOptionsItemSelected(item);
		}
	}
	@Override
	protected void onDestroy() {
		CloudKernelApplication.getInstance().deInitData();
		super.onDestroy();
	}
	@Override
	protected void onUpdateFinished() {
		
		Log.d(TAG, "we might want to know when the sync is finished");
	}
	@Override
	protected void onUpdateStarted() {
		// To do. register for updates to the sync manager.
		//we might need it.
		Log.d(TAG, "we might want to know the sync has started");
	}
	@Override
	protected void initLayout() {
		setContentView(R.layout.startmenu_activity);
	}
	
	@Override
	protected void registerForUpdates() {
		Log.d(TAG, "we don't want to listen for any kind of updates");
	}
	@Override
	protected void register() {
		getSyncWorkerPublisher().register(this);
	}
	@Override
	protected void unregister() {
		getSyncWorkerPublisher().unregister(this);
	}
	
}
