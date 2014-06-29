package com.google.cloud.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.R;
import com.google.cloud.adapters.ElementAdapter;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.provider.IDataProvider;
import com.google.cloud.publisher.IElementChangeListener;

public abstract class ManageElements extends CloudActivity implements
		IElementChangeListener {
	private ElementAdapter adapter;
	private ViewGroup contentView = null;
	protected List<BaseElement> dataList = new ArrayList<BaseElement>();
	protected static final String TAG = ManageElements.class.getSimpleName();
	ListView elementList = null;
	TextView emptyTextView = null;
	private View loadingView;
	protected abstract ObjectType getObjectType();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
		initializeData();
		initializeAdapter();
		// initializeLoadingView();
	}

	@Override
	protected void registerForUpdates() {
		
	}

	@Override
	protected void initLayout() {
		contentView = (ViewGroup) LayoutInflater.from(this).inflate(
				R.layout.manage_element_activity, null);
		setContentView(contentView);
	}

	private void initializeLoadingView() {
		LayoutInflater inflater = (LayoutInflater) CloudKernelApplication
				.getInstance()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		loadingView = (View) inflater.inflate(R.layout.loading_view, null);
		if (loadingView != null) {
			TextView txtView = (TextView) loadingView
					.findViewById(R.id.tv_loading_text);
			if (txtView != null) {
				txtView.setText("Loading...");
			}
			if (isUpdateInProgress) {
				emptyTextView.setVisibility(View.GONE);
				contentView.addView(loadingView);
			}
		}
	}
	@Override
	protected void onResume() 
	{
		super.onResume();
		getChangePublisher().register(this);
	}
	@Override
	protected void onPause() {
		getChangePublisher().unregister(this);
		super.onPause();
	}

	protected IDataProvider getDataProvider() {
		return CloudKernelApplication.getInstance().getDataProvider();
	}

	protected abstract void initializeData();

	private void initializeAdapter() {
		adapter = new ElementAdapter(this, dataList);
		elementList = (ListView) this.findViewById(R.id.lv_manage_element_list);
		elementList.setAdapter(adapter);
		emptyTextView = (TextView) this.findViewById(android.R.id.empty);
		emptyTextView.setText("No data available");
		updateEmptyViewVisibility();
	}

	public void updateEmptyViewVisibility() {
		updateEmptyViewVisibility(elementList, emptyTextView);
	}

	@Override
	public void onElementChanged(final BaseElement element) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(element.getObjectType().equals(getObjectType().getType()))
				{
					updateView(element);
				}
			}
		});
	}

	protected void updateView(BaseElement element) {
		Log.d(TAG, "updating the view.manageElements");
		notifyAdaptersContentChanged(element);
	}

	protected void notifyAdaptersContentChanged(BaseElement element) {
		adapter.onContentChanged(element);
	}

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
	protected void onUpdateFinished() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				contentView.removeView(loadingView);
				updateEmptyViewVisibility(elementList, emptyTextView);
			}
		});
	}
}
