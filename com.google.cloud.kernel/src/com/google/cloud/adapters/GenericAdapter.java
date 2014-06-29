package com.google.cloud.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.cloud.utils.ComparationUtils;

public abstract class GenericAdapter<T> extends BaseAdapter {

	private static final String TAG = GenericAdapter.class.getSimpleName();
	private Context context;
	protected Object adapterLock = new Object();
	private List<RowView<T>> rowViews = null;

	public GenericAdapter(Context context, List<T> objects) {
		this.context = context;
		rowViews = new ArrayList<RowView<T>>();
		initViews(objects);
	}

	private void initViews(List<T> objects) {
		synchronized (adapterLock) {
			if (objects == null) {
				return;
			}
			for (T object : objects) {
				if (!isObjectInRowViewList(object) && isObjectVisible(object)) {
					rowViews.add(createRowView(object));
				}
			}
		}
	}
	protected abstract boolean isObjectVisible(T object);

	protected List<T> getCollectionObjects()
	{
		List<T> objects = new ArrayList<T>();
		for(RowView<T> row : getRowViews())
		{
			if(row.getObject()!=null)
			{
				objects.add(row.getObject());
			}
		}
		return objects;
	}
	protected boolean isObjectInRowViewList(T object) {
		boolean exist = false;
		for (RowView<T> row : rowViews) {
			if (row.getObject().equals(object)) {
				exist = true;
			}
		}
		return exist;
	}

	public void removeAllViews() {
		boolean isChanged = false;
		synchronized (adapterLock) {
			if (getRowViews() != null && !getRowViews().isEmpty()) {
				getRowViews().clear();
				isChanged = true;
			}
			if (isChanged) {
				notifyDataSetChanged();
			}
		}
	}
	protected void remove(T object) {
		boolean isChanged = false;
		synchronized (adapterLock) {
			if (object != null && getRowViews() != null
					&& !getRowViews().isEmpty()) {
				for (RowView<?> row : getRowViews()) {
					if (ComparationUtils.areObjectsEqual(row.getObject(),
							object)) {
						getRowViews().remove(row);
						isChanged = true;
					}
				}
			}
			if (isChanged) {
				notifyDataSetChanged();
			}
		}
	}

	protected void add(T object) {
		boolean isChanged = false;
		synchronized (adapterLock) {
			if (object != null && getRowViews() != null) {
				RowView<T> rowToUpdate = createRowView(object);
				if (!rowViews.contains(rowToUpdate)) {
					rowViews.add(rowToUpdate);
					isChanged = true;
				}
			}
			if (isChanged) {
				notifyDataSetChanged();
			}
		}
	}

	public void clear() {
		synchronized (adapterLock) {
			if (rowViews != null && !rowViews.isEmpty()) {
				rowViews.clear();
				notifyDataSetChanged();
			}
		}
	}

	protected abstract LayoutInflater getInflater();

	protected List<RowView<T>> getRowViews() {
		return rowViews;
	}

	@Override
	public int getCount() {
		return rowViews.size();
	}

	protected Context getContext() {
		return this.context;
	}

	@Override
	public Object getItem(int position) {
		return rowViews.get(position).getObject();
	}

	protected RowView<T> getRowAtPosition(int position) {
		return rowViews.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	protected static class ViewHolder {

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RowView<T> rowView = null;
		ViewHolder holder = null;
		synchronized (adapterLock) {
			rowView = getRowViews().get(position);
		}
		if (convertView == null) {
			LayoutInflater inflater = getInflater();
			if (inflater != null) {
				convertView = inflater.inflate(getResource(), null, false);
				holder = createViewHolder(convertView);
			}
		} else {
			holder = getViewHolder(convertView);
		}
		updateView(rowView, holder);
		setClickListeners(convertView, rowView.getObject());
		return convertView;
	}

	protected abstract void setClickListeners(View convertView, T object);

	protected abstract void updateView(RowView<T> convertView, ViewHolder holder);

	protected abstract int getResource();

	protected abstract ViewHolder getViewHolder(View rowView);

	protected abstract ViewHolder createViewHolder(View rowView);

	protected abstract RowView<T> createRowView(T object);
}