package com.google.cloud.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.R;
import com.google.cloud.adapters.PropertyAdapter;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.client.properties.StringProperty;
import com.google.cloud.publisher.IElementChangeListener;

public class CreateComputeActivity extends CloudActivity implements IElementChangeListener
{
	protected List<StringProperty> dataList = new ArrayList<StringProperty>();
	private Compute compute = null;
	private ViewGroup contentView = null;
	private PropertyAdapter adapter;
	private String elementId;
	private ListView listView;
	private TextView emptyTextView;
	private static final String TAG = CreateComputeActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		initLayout();
		registerForUpdates();
		createEmptyObject();
		initializeAdapter();
		addPropertiesToCompute();
		updateElementById(compute.getElementId(), true);
	}
	private void createEmptyObject()
	{
		compute = getDataProvider().createEmptyCompute();
		this.elementId = compute.getElementId();
	}
	private void addPropertiesToCompute()
	{
		getDataProvider().addPropertiesToCompute(compute);
	}
	protected void getBaseElementById(String uid) {
		getDataProvider().getComputeElementById(uid, true);
	}
	private void initializeAdapter()
	{
		adapter = new PropertyAdapter(this,elementId, dataList);
		listView = (ListView)this.findViewById(R.id.lv_baseelement_list);
		listView.setAdapter(adapter);
		emptyTextView = (TextView)this.findViewById(android.R.id.empty);

	}

	protected void updateElementById(String uid, boolean update) {
		Compute compute = getDataProvider().getComputeElementById(uid, false);
		if(compute!=null && compute.getPropertyList()!=null && !compute.getPropertyList().isEmpty())
		{
			dataList.addAll(compute.getPropertyList());
		}
		else
		{
			getDataProvider().getComputeElementById(uid, true);
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		inflateOptionsMenu(menu, R.menu.compute_activity_menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId()==R.id.create_compute)
		{
			String elementId = null;
			if(compute!=null)
			{
				elementId = compute.getElementId();
				if(elementId!=null && !elementId.equals(""))
				{
					getDataProvider().commitComputeToserver(elementId);
				}
			}
		}
		return true;
	}

	protected void updateBaseElementCache(BaseElement element) {
		getDataCache().addComputeToCache((Compute)element);
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
	protected void registerForUpdates() {
		CloudKernelApplication.getInstance().getChangePublisher().register(this);
	}
	private String getObjectType()
	{
		return ObjectType.COMPUTE.getType();
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
					updateView(element);
				}
				else
				{
					Log.d(TAG, "different type received");
				}
			}
		});
	}
	protected void updateView(BaseElement element)
	{
		Log.d(TAG, "updating the view.BaseElementActivity");
		notifyAdaptersContentChanged(element);
	}
	protected void notifyAdaptersContentChanged(final BaseElement element) {
		adapter.refreshContents(element);	
	}
	@Override
	protected void initLayout() {
		contentView = (ViewGroup) LayoutInflater.from(this).inflate(
				R.layout.base_element_activity, null);
		setContentView(contentView);
	}
	@Override
	protected void onUpdateStarted() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onUpdateFinished() {
		// TODO Auto-generated method stub
		
	}

}
