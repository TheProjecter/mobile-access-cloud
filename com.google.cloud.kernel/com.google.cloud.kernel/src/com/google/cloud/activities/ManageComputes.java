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

public class ManageComputes extends ManageElements {
	private static final String TAG = ManageComputes.class.getSimpleName();
	@Override
	protected void initializeData() {
		List<BaseElement> listOfComputes = getDataProvider().getCollections(CollectionType.COMPUTE_COLLECTIONS,false);
		if(listOfComputes!=null)
		{
			for(BaseElement baseElement : listOfComputes)
			{
				if(baseElement!=null)
				{
					this.dataList.add(baseElement);
				}
			}
		}
		else
		{
			getDataProvider().getCollections(CollectionType.COMPUTE_COLLECTIONS, true);
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
			CloudKernelApplication.getInstance().getDataProvider().getCollections(CollectionType.COMPUTE_COLLECTIONS, true);
		}
		return true;
	}

	@Override
	protected ObjectType getObjectType() {
		return ObjectType.COMPUTE;
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
