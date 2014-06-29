package com.google.cloud.synchronization.task;

import android.util.Log;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.provider.IDataProvider;
import com.google.cloud.webservice.IWebServiceClient;
import com.google.cloud.webservice.WebServiceClient;

public class CommitNetworkSync extends SyncTask {

	private ObjectType objectType;
	public CommitNetworkSync(String elementId, ObjectType type)
	{
		super(SyncAction.COMMIT_NETWORK.getActionType(), elementId);
		this.objectType = type;
	}
	@Override
	public void synchronize() throws Exception {
		Log.d(TAG, "commit a network object with the id : " + this.getElementId());
		BaseElement baseElementToUpdate = null;
		IDataProvider provider = CloudKernelApplication.getInstance().getDataProvider();
		baseElementToUpdate = provider.getBaseElementById(this.getElementId(),objectType, false);
		IWebServiceClient client = WebServiceClient.getInstance();
		client.createBaseElement(baseElementToUpdate);
		synchronized (SYNC_LOCK) {
			
		}
	}

}
