package com.google.cloud.activities;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.R;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.objects.ObjectType;

public class ManageStorages extends ManageElements{
	private static final String TAG = ManageStorages.class.getSimpleName();
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}
	 @Override
	protected ObjectType getObjectType() {
		return ObjectType.STORAGE;
	}
	@Override
	protected void initializeData() {
		List<BaseElement> listOfStorages = getDataProvider().getCollections(CollectionType.STORAGE_COLLECTIONS, false);
		if(listOfStorages!=null)
		{
			for(BaseElement storage : listOfStorages)
			{	
				if (storage != null) 
				{
					this.dataList.add(storage);
				}
			}
		}
		else 
		{
			getDataProvider().getCollections(CollectionType.STORAGE_COLLECTIONS, true);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		inflateOptionsMenu(menu, R.menu.storages_menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId()==R.id.force_update)
		{
			Log.d(TAG, "Force an update.");
			//CloudKernelApplication.getInstance().getDataProvider().initializeData();
			CloudKernelApplication.getInstance().getDataProvider().getCollections(CollectionType.STORAGE_COLLECTIONS,true);
		}
		return true;
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
