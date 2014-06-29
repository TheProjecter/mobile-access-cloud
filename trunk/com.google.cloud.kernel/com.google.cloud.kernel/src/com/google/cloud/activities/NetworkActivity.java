package com.google.cloud.activities;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.adapters.PropertyAdapter;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.ObjectType;

import android.os.Bundle;
import android.util.Log;

public class NetworkActivity extends BaseElementActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void registerForUpdates() {
		CloudKernelApplication.getInstance().getChangePublisher().register(this);
	}
	@Override
	protected void updateElementById(String uid, boolean update) {
		Log.d(TAG, "showing the Network data for a specific id");
		BaseElement baseElement = getDataProvider().getBaseElementById(uid,ObjectType.NETWORK, false);
		if(baseElement!=null && baseElement.getPropertyList()!=null && !baseElement.getPropertyList().isEmpty())
		{
			dataList.addAll(baseElement.getPropertyList());
		}
		else
		{
			getDataProvider().getBaseElementById(uid,ObjectType.NETWORK, true);
		}
		
	}
	@Override
	protected void getBaseElementById(String elementId) {
		getDataProvider().getBaseElementById(elementId, ObjectType.NETWORK, true);
	}
	@Override
	protected void register() {
		getSyncWorkerPublisher().register(this);
	}
	@Override
	protected void unregister() {
		getSyncWorkerPublisher().unregister(this);
	}
	@Override
	protected void updateBaseElementCache(BaseElement baseElement)
	{
		getDataCache().addBaseElementToCache(baseElement);
	}
	@Override
	protected void notifyAdaptersContentChanged(BaseElement element) {
		((PropertyAdapter)getAdapter()).refreshContents(element);	
	}

}
