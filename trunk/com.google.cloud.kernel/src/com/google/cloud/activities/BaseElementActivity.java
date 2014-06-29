package com.google.cloud.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.R;
import com.google.cloud.adapters.GenericAdapter;
import com.google.cloud.adapters.PropertyAdapter;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.properties.StringProperty;
import com.google.cloud.publisher.IElementChangeListener;

public abstract class BaseElementActivity extends CloudActivity implements IElementChangeListener{
	private String elementId;
	protected static final String TAG = BaseElementActivity.class.getSimpleName();
	private ViewGroup contentView = null;
	private PropertyAdapter adapter;
	private ListView networkList;
	private TextView emptyTextView;
	private View loadingView;
	protected List<StringProperty> dataList = new ArrayList<StringProperty>();
	private String objectType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
		registerForUpdates();
		initializeData();
	}
	private void initializeData()
	{
		Intent intent = this.getIntent();
		if(intent.getExtras()!=null)
		{
			this.elementId = intent.getExtras().getString(CloudKernelApplication.getInstance().getString(R.string.uid));
			this.objectType = intent.getExtras().getString(CloudKernelApplication.getInstance().getString(R.string.type));
			updateElementById(elementId, false);
		}
		initializeAdapter();
	}
	@Override
	protected void onPause() 
	{
		getChangePublisher().unregister(this);
		super.onPause();
	}
	protected GenericAdapter<?> getAdapter()
	{
		return this.adapter;
	}
	private void initializeLoadingView()
	{
		LayoutInflater inflater = (LayoutInflater)CloudKernelApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		loadingView = (View)inflater.inflate(R.layout.loading_view, null);
		if(loadingView!=null)
		{
			TextView txtView = (TextView)loadingView.findViewById(R.id.tv_loading_text);
			if(txtView!=null)
			{
				txtView.setText("Loading...");
			}
			if(isUpdateInProgress)
			{
				emptyTextView.setVisibility(View.GONE);
				contentView.addView(loadingView);
			}
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		inflateOptionsMenu(menu, R.menu.network_activity_menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.menu_update)
		{
			Log.d(TAG, "update the current content.");
			adapter.removeAllViews();
			getBaseElementById(elementId);
		}
		return true;
	}
	protected abstract void getBaseElementById(String elementId);
	@Override
	protected void initLayout() {
		contentView = (ViewGroup) LayoutInflater.from(this).inflate(
				R.layout.base_element_activity, null);
		setContentView(contentView);
	}
	private void initializeAdapter()
	{
		adapter = new PropertyAdapter(this,elementId, dataList);
		networkList = (ListView)this.findViewById(R.id.lv_baseelement_list);
		networkList.setAdapter(adapter);
		emptyTextView = (TextView)this.findViewById(android.R.id.empty);

	}
	public void updateEmptyViewVisibility()
	{
		updateEmptyViewVisibility(networkList, emptyTextView);
	}
	protected abstract void updateElementById(String uid, boolean update);
	protected String getObjectType()
	{
		return this.objectType;
	}
	@Override
	public void onElementChanged(final BaseElement element) 
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				if (element.getObjectType().equals(getObjectType()))
				{
					Log.d(TAG, "update element with id:" + element.getElementId());	
					Log.d(TAG, "type of the object is : " + element.getObjectType());
					updateView(element);
				}
				else
				{
					Log.d(TAG, "different type received");
				}
			}
		});
	}
	
	protected abstract void updateBaseElementCache(BaseElement element);
	protected void updateView(BaseElement element)
	{
		Log.d(TAG, "updating the view.BaseElementActivity.element type" + element.getObjectType());
		notifyAdaptersContentChanged(element);
	}
	protected abstract void notifyAdaptersContentChanged(BaseElement element);
	@Override
	protected void onUpdateStarted() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				initializeLoadingView();	
			}
		});
	}
	@Override
	protected void onUpdateFinished()
	{
		runOnUiThread(new Runnable()
		{	
			@Override
			public void run() {
				contentView.removeView(loadingView);
				updateEmptyViewVisibility(networkList, emptyTextView);
			}
		});
	}

}
