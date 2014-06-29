package com.google.cloud.activities;

import java.util.List;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.R;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.objects.ObjectType;

public class ManageUsers extends ManageElements {

	@Override
	protected ObjectType getObjectType() {
		return ObjectType.USER;
	}

	@Override
	protected void initializeData() {
		List<BaseElement> listOfBaseElements = getDataProvider().getCollections(CollectionType.USER_COLLECTIONS, false);
		if(listOfBaseElements!=null)
		{
			for (BaseElement baseElement : listOfBaseElements) 
			{
				if (baseElement != null) {
					this.dataList.add(baseElement);
				}
			}
		}
		else
		{
			getDataProvider().getCollections(CollectionType.USER_COLLECTIONS, true);
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		inflateOptionsMenu(menu, R.menu.base_elements_menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId()==R.id.force_update)
		{
			Log.d(TAG, "Force an update.");
			//CloudKernelApplication.getInstance().getDataProvider().initializeData();
			CloudKernelApplication.getInstance().getDataProvider().getCollections(CollectionType.USER_COLLECTIONS, true);
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
