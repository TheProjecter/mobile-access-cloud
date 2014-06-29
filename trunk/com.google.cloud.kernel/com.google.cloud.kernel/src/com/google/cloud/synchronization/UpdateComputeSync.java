package com.google.cloud.synchronization;

import android.util.Log;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.provider.IDataProvider;
import com.google.cloud.synchronization.task.SyncAction;
import com.google.cloud.synchronization.task.SyncTask;
import com.google.cloud.webservice.IWebServiceClient;

public class UpdateComputeSync extends SyncTask {

	public UpdateComputeSync(String elementId) {
		super(SyncAction.UPDATE_COMPUTE.getActionType(), elementId);
	}

	@Override
	public void synchronize() throws Exception {
		Log.d(TAG, "getting a compute element");
		Compute compute = null;
		IDataProvider provider = CloudKernelApplication.getInstance().getDataProvider();
		IWebServiceClient client = CloudKernelApplication.getInstance().getWebServiceClient();
		String elementId = getElementId();
		compute = client.getComputeById(elementId);
		synchronized (SyncTask.SYNC_LOCK) {
			if(compute!=null)
			{
				Log.d(TAG, "update finished");
				provider.insertComputeToDbIfNotExist(compute);
			}
		}
	}

}
