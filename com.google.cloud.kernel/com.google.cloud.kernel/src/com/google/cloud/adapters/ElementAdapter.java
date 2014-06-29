package com.google.cloud.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.R;
import com.google.cloud.activities.ComputeActivity;
import com.google.cloud.activities.ManageComputes;
import com.google.cloud.activities.NetworkActivity;
import com.google.cloud.activities.StorageActivity;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.utils.ComparationUtils;

public class ElementAdapter extends GenericAdapter<BaseElement> {

	protected static final String TAG = ElementAdapter.class.getSimpleName();

	public ElementAdapter(Context context, List<BaseElement> objects) {
		super(context, objects==null || objects.isEmpty() ? null : objects);
	}
	@Override
	protected LayoutInflater getInflater() {
		return LayoutInflater.from(getContext());
	}
	public static class BaseViewHolder extends ViewHolder
	{
		private TextView element_header;
		private TextView element_description;
		public TextView element_name;
	}
	@Override
	protected void setClickListeners(final View rowView, final BaseElement element) {
		rowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// for now we have only support for network collections.
				if(element.getObjectType().equals(ObjectType.NETWORK.getType()))
				{
					String id = element.getElementId();
					if(id!=null)
					{
						Intent intent = new Intent(getContext(), NetworkActivity.class);
						intent.putExtra(CloudKernelApplication.getInstance().getString(R.string.uid), id);
						intent.putExtra(CloudKernelApplication.getInstance().getString(R.string.type), element.getObjectType());
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						CloudKernelApplication.getInstance().startActivity(intent);
					}
				}
				else if(element.getObjectType().equals(ObjectType.STORAGE.getType()))
				{					
					Log.d(TAG, "the element is of storage type");
					String id = element.getElementId();
					Log.d(TAG, "id of the storage is : " + id);
					if(id!=null)
					{
						Intent intent = new Intent(getContext(), StorageActivity.class);
						intent.putExtra(CloudKernelApplication.getInstance().getString(R.string.uid), id);
						intent.putExtra(CloudKernelApplication.getInstance().getString(R.string.type), element.getObjectType());
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						CloudKernelApplication.getInstance().startActivity(intent);
					}
				}
				else if(element.getObjectType().equals(ObjectType.COMPUTE.getType()))
				{
					Log.d(TAG, "the element is of compute type");
					String id = element.getElementId();
					if(id!=null)
					{
						Intent intent = new Intent(getContext(), ComputeActivity.class);
						intent.putExtra(CloudKernelApplication.getInstance().getString(R.string.uid), id);
						intent.putExtra(CloudKernelApplication.getInstance().getString(R.string.type), element.getObjectType());
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						CloudKernelApplication.getInstance().startActivity(intent);
								
					}
				}
			}
		});
	}
	public void onContentChanged(BaseElement element)
	{
		synchronized (adapterLock) 
		{
			if(ComparationUtils.uidBasedIndexOf(getBaseElements(), element) < 0)
			{
				add(element);
			}
		}
	}
	@Override
	protected void add(BaseElement object) {

		super.add(object);
	}
	private List<BaseElement> getBaseElements()
	{
		List<BaseElement> baseElements = new ArrayList<BaseElement>();
		for(RowView<BaseElement> row : getRowViews())
		{
			if(row.getObject()!=null)
			{
				baseElements.add(row.getObject());
			}
		}
		return baseElements;
	}
	@Override
	protected void updateView(RowView<BaseElement> baseElementRowView, ViewHolder holder) {
		BaseViewHolder baseHolder = (BaseViewHolder)holder;
		if(baseHolder!=null &&  baseElementRowView!=null)
		{
			baseHolder.element_header.setText(baseElementRowView.getObject().getObjectType());
			baseHolder.element_description.setText("Name : ");
			baseHolder.element_name.setText(baseElementRowView.getObject().getName());
		}
	}
	@Override
	protected int getResource() {
		return R.layout.manage_element_list_entry;
	}

	@Override
	protected ViewHolder getViewHolder(View rowView) {
		ViewHolder holder = (ViewHolder)rowView.getTag();
		return holder;
	}

	@Override
	protected ViewHolder createViewHolder(View rowView) {
		BaseViewHolder holder = new BaseViewHolder();
		holder.element_header = (TextView)rowView.findViewById(R.id.tv_elememnt_header);
		holder.element_description = (TextView)rowView.findViewById(R.id.txtview_element_description);
		holder.element_name = (TextView)rowView.findViewById(R.id.txtview_element_name);
		rowView.setTag(holder);
		return holder;
	}

	@Override
	protected RowView<BaseElement> createRowView(BaseElement object) {
		return new RowView<BaseElement>(object);
	}
	@Override
	protected boolean isObjectVisible(BaseElement object) {
		if(object.isVisible())
		{
			return true;
		}
		return false;
	}

}