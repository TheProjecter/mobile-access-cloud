package com.google.cloud.editor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.client.properties.StringProperty;
import com.google.cloud.provider.DataCache;

public class EditorDialogManager implements IEditorDialogManager, IEditorDialogCallback {
	private static EditorDialogManager instance = null;
	private Dialog dialog;
	private Context context;
	private Map<UUID, StringProperty> mapOfPropertiesToEdit;
	private static final String TAG = EditorDialogManager.class.getSimpleName();
	private static final String message = "Edit";
	protected EditorDialogManager()
	{
		mapOfPropertiesToEdit = new HashMap<UUID, StringProperty>();
	}
	public static EditorDialogManager getInstance()
	{
		if(instance==null)
		{
			instance = new EditorDialogManager();
		}
		return instance;
	}
	public UUID show(Context context, StringProperty property)
	{
		this.context = context;
		UUID randomUid = UUID.randomUUID();
		mapOfPropertiesToEdit.put(randomUid, property);
		dialog = createDialog(context,randomUid, property.getValue());
		if(dialog!=null)
		{
			showDialog();
		}
		return randomUid;
	}
	private Dialog createDialog(Context context, UUID randomUid, String value)
	{
		return new CreateAlertDialog(context, message, value,randomUid, this);
	}
	private void showDialog()
	{
		if(dialog!=null)
		{
			dialog.show();
		}
	}
	@Override
	public void onEditorFinished(UUID editorID, String originalValue,
			String newValue, EditorAction action) {
		if(action==EditorAction.OK)
		{
			StringProperty prop = mapOfPropertiesToEdit.get(editorID);
			if(prop!=null && newValue!=null && !newValue.equals("") && !newValue.equals(originalValue))
			{
				prop.setValue(newValue);
				BaseElement elem = prop.getOwner();
				elem.updatePropertyValue(prop);
				elem.update();
				if(elem.getObjectType().equals(ObjectType.COMPUTE.getType()))
				{
					DataCache.getInstance().addComputeToCache((Compute)elem);
				}
				else if(elem.getObjectType().equals(ObjectType.NETWORK.getType()) ||
						elem.getObjectType().equals(ObjectType.STORAGE.getType()))
				{
					DataCache.getInstance().addBaseElementToCache(elem);
				}
			}
			Log.d(TAG, "The user has confirmed the modifications");
		}
		else if(action==EditorAction.CANCEL)
		{
			StringProperty prop = mapOfPropertiesToEdit.get(editorID);
			prop.setValue(originalValue);
			Log.d(TAG, "the user has chosen to cancel the operation");
		}
		else 
			return;
	}
}
