package com.google.cloud.synchronization.task;

import android.util.Log;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.provider.IDataProvider;
import com.google.cloud.webservice.IWebServiceClient;
import com.google.cloud.webservice.WebServiceClient;

public class CommitComputeTaskSync extends SyncTask 
{
	private ObjectType objectType;
	public CommitComputeTaskSync(String elementId, ObjectType type) {
		super(SyncAction.COMMIT_COMPUTE.getActionType(), elementId);
		this.objectType = type;
	}

	@Override
	public void synchronize() throws Exception {
		Log.d(TAG, "commit a compute object with the id : " + this.getElementId());
		Compute computeToUpdate = null;
		IDataProvider provider = CloudKernelApplication.getInstance().getDataProvider();
		computeToUpdate = provider.getComputeElementById(this.getElementId(), false);
		IWebServiceClient client = WebServiceClient.getInstance();
		client.createComputeElement(computeToUpdate);
		synchronized (SYNC_LOCK) {
			
		}
		
	}
	
}
