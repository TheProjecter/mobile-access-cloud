package com.google.cloud.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.R;
import com.google.cloud.adapters.PropertyAdapter;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.ObjectType;

public class ComputeActivity extends BaseElementActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void getBaseElementById(String elementId) {
		getDataProvider().getComputeElementById(elementId, true);
	}

	@Override
	protected void updateElementById(String uid, boolean update) {
		Log.d(TAG, "showing the Network data for a specific id");
		Compute computeElement = getDataProvider().getComputeElementById(uid, false);
		if(computeElement!=null && computeElement.getPropertyList()!=null && !computeElement.getPropertyList().isEmpty())
		{
			dataList.addAll(computeElement.getPropertyList());
		}
		else
		{
			getDataProvider().getComputeElementById(uid, true);
		}
	}
	@Override
	protected void notifyAdaptersContentChanged(final BaseElement element) {
		((PropertyAdapter)getAdapter()).refreshContents(element);	
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
	public boolean onCreateOptionsMenu(Menu menu) {
		inflateOptionsMenu(menu, R.menu.compute_activity_menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.create_compute)
		{
			startCreateComputeActivity();
		}
		return super.onOptionsItemSelected(item);
	}
	private void startCreateComputeActivity()
	{
		Intent intent = new Intent(ComputeActivity.this, CreateComputeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		CloudKernelApplication.getInstance().startActivity(intent);
	}
	@Override
	protected void registerForUpdates() {
		CloudKernelApplication.getInstance().getChangePublisher().register(this);
	}
	@Override
	protected void updateBaseElementCache(BaseElement baseElement)
	{
		getDataCache().addBaseElementToCache(baseElement);
	}
	@Override
	protected String getObjectType() {
		if(super.getObjectType()==null)
		{
			return ObjectType.COMPUTE.getType();
		}
		return super.getObjectType();
	}

}
