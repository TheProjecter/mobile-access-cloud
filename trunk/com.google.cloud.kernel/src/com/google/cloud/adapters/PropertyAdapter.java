package com.google.cloud.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.R;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.properties.StringProperty;
import com.google.cloud.editor.EditorDialogManager;
import com.google.cloud.provider.IDataProvider;
import com.google.cloud.utils.DisplayStrings;

public class PropertyAdapter extends GenericAdapter<StringProperty>{

	private String parentId;
	private static final String TAG = PropertyAdapter.class.getSimpleName();
	public PropertyAdapter(Context context, String parentId,
			List<StringProperty> objects) {
		super(context, objects == null || objects.isEmpty() ? objects = null
				: objects);
		this.parentId = parentId;
	}
	public void refreshContents(BaseElement element) {
		
		if (parentId==null || !element.getElementId().equals(parentId)) {
			return;
		}
		if (element != null) {
			if(!element.hasProperties())
			{
				return;
			}
			clear();
			List<StringProperty> properties = element.getPropertyList();
			for (StringProperty prop : properties) {
				if (prop != null && !isObjectInRowViewList(prop) && prop.getIsVisible()) {
					add(prop);
					Log.d(TAG, "refreshContents.notifyDataset");
				}
			}
			notifyDataSetChanged();
		}
	}
	
	private int getResourceId() {
		return R.layout.property_entry;
	}

	public static class PropertyHolder extends ViewHolder {
		private TextView key;
		private TextView value;
	}
	@Override
	protected void setClickListeners(View rowView, StringProperty prop) {
		if(prop.isChangeAllowed())
		{
			rowView.setOnClickListener(createEditPropListener(prop));
		}
		else
			rowView.setOnClickListener(null);
	}
	public View.OnClickListener createEditPropListener(final StringProperty prop)
	{
		return new View.OnClickListener()
		{	
			@Override
			public void onClick(View v) {
				Log.d(TAG, "property: " + prop.getValue() + " can be changed");
				EditorDialogManager.getInstance().show(getContext(), prop);
			}
		};
	}
	@Override
	protected void updateView(RowView<StringProperty> property,
			ViewHolder holder) {
		PropertyHolder propholder = (PropertyHolder) holder;
		if (propholder != null && property != null) {
			propholder.key.setText(DisplayStrings
					.getRealKeyForProperty(property.getObject()));
			propholder.value.setText(property.getObject().getValue());
		}
	}

	@Override
	protected int getResource() {
		return getResourceId();
	}

	@Override
	protected ViewHolder getViewHolder(View rowView) {
		ViewHolder holder = (ViewHolder) rowView.getTag();
		return holder;
	}

	@Override
	protected ViewHolder createViewHolder(View rowView) {
		PropertyHolder propHolder = new PropertyHolder();
		propHolder.key = (TextView) rowView
				.findViewById(R.id.txtview_property_entry_key);
		propHolder.value = (TextView) rowView
				.findViewById(R.id.txtview_property_entry_value);
		rowView.setTag(propHolder);
		return propHolder;
	}

	@Override
	protected RowView<StringProperty> createRowView(StringProperty object) {
		return new RowView<StringProperty>(object);
	}

	@Override
	protected LayoutInflater getInflater() {
		return LayoutInflater.from(getContext());
	}
	protected IDataProvider getDataProvider()
	{
		return CloudKernelApplication.getInstance().getDataProvider();
	}
	@Override
	protected boolean isObjectVisible(StringProperty object) {
		if(object.getIsVisible())
		{
			return true;
		}
		return false;
	}
}