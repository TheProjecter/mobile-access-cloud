package com.google.cloud.ui.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.R;
import com.google.cloud.activities.ManageComputes;
import com.google.cloud.activities.ManageNetworks;
import com.google.cloud.activities.ManageStorages;
import com.google.cloud.activities.ManageUsers;
import com.google.cloud.adapters.GenericAdapter;
import com.google.cloud.adapters.RowView;
import com.google.cloud.utils.ItemValuePair;
import com.google.cloud.utils.constants.GeneralConstants;

public class StartMenuAdapter extends GenericAdapter<ItemValuePair> {

	public StartMenuAdapter(Context context, List<ItemValuePair> objects) {
		super(context, objects == null || objects.isEmpty() ? null : objects);
	}
	public static class ItemValueHolder extends ViewHolder
	{
		private TextView keyName;
		private TextView keyValue;
	}
	@Override
	protected LayoutInflater getInflater() {
		return LayoutInflater.from(getContext());
	}
	@Override
	protected void updateView(RowView<ItemValuePair> itemValueRow, ViewHolder holder) 
	{
		if(itemValueRow!=null && holder!=null)
		{
			ItemValueHolder itemHolder = (ItemValueHolder)holder;
			itemHolder.keyName.setText(itemValueRow.getObject().getKey());
			itemHolder.keyValue.setText(itemValueRow.getObject().getValue());
		}
	}

	@Override
	protected int getResource() {
		return R.layout.itemvalue_layout;
	}

	@Override
	protected ViewHolder getViewHolder(View rowView) {
		ViewHolder holder = (ViewHolder)rowView.getTag();
		return holder;
	}

	@Override
	protected ViewHolder createViewHolder(View rowView) {
		ItemValueHolder itemHolder = new ItemValueHolder();
		itemHolder.keyName = (TextView)rowView.findViewById(R.id.txtview_item_entry_key);
		itemHolder.keyValue = (TextView)rowView.findViewById(R.id.txtview_item_entry_value);
		return itemHolder;
	}

	@Override
	protected RowView<ItemValuePair> createRowView(ItemValuePair object) {
		return new RowView<ItemValuePair>(object);
	}
	@Override
	protected void setClickListeners(final View rowView,
			final ItemValuePair object) {
		rowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// for now we have only support for network collections.
				if (object.getKey().equals(GeneralConstants.NETWORK_COLLECTION)) {
					Intent intent = new Intent(CloudKernelApplication.getInstance().getApplicationContext(),
											   ManageNetworks.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					CloudKernelApplication.getInstance().startActivity(intent);
				} else if (object.getKey().equals(GeneralConstants.STORAGE_COLLECTION)) {
					Intent intent = new Intent(CloudKernelApplication.getInstance().getApplicationContext(), 
											   ManageStorages.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					CloudKernelApplication.getInstance().startActivity(intent);
				}
				else if(object.getKey().equals(GeneralConstants.COMPUTE_COLLECTION))
				{
					Intent intent = new Intent(CloudKernelApplication.getInstance().getApplicationContext(),
											  ManageComputes.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					CloudKernelApplication.getInstance().startActivity(intent);
				}
				else if(object.getKey().equals(GeneralConstants.USER_COLLECTION))
				{
					Intent intent = new Intent(CloudKernelApplication.getInstance().getApplicationContext(), ManageUsers.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					CloudKernelApplication.getInstance().startActivity(intent);
				}
			}
			
		});
	}
	@Override
	protected boolean isObjectVisible(ItemValuePair object) {
		return true;
	}
}