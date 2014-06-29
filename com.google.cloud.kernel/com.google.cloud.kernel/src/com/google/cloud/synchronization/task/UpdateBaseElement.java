package com.google.cloud.synchronization.task;

import android.util.Log;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.provider.IDataProvider;
import com.google.cloud.webservice.IWebServiceClient;

public class UpdateBaseElement extends SyncTask
{
	private ObjectType objectType;
	public UpdateBaseElement(String elementId, ObjectType type) {
		super(SyncAction.UPDATE_NETWORK.getActionType() ,elementId);
		this.objectType = type;
	}

	@Override
	public void synchronize() throws Exception {
		Log.d(TAG, "get a BaseElement object by its elementid");
		BaseElement baseElement = null;
		IDataProvider provider = CloudKernelApplication.getInstance().getDataProvider();
		IWebServiceClient webClient = CloudKernelApplication.getInstance().getWebServiceClient();
		String elementId = getElementId();
		baseElement = webClient.getBaseElementById(elementId, objectType);
		synchronized (SyncTask.SYNC_LOCK) {
			if(baseElement!=null)
			{
				provider.insertElementToDbIfNotFound(baseElement);
			}
		}
	}
	
}
