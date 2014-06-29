package com.google.cloud.synchronization.task;

import java.util.List;

import android.util.Log;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.provider.IDataProvider;
import com.google.cloud.webservice.IWebServiceClient;

public class UpdateBaseElementCollectionTask extends SyncTask {

	private CollectionType type;
	public UpdateBaseElementCollectionTask(CollectionType type) {
		super(SyncAction.UPDATE_NETWORK_COLLECTION.getActionType(),null);
		this.type = type;
	}

	@Override
	public void synchronize() throws Exception 
	{
		Log.d(TAG,"update a collection of networks");
		List<BaseElement> baseElementList = null;
		IDataProvider provider = CloudKernelApplication.getInstance().getDataProvider();
		IWebServiceClient wsClient = CloudKernelApplication.getInstance().getWebServiceClient();
		baseElementList = wsClient.getBaseElementCollections(type);
		synchronized(SyncTask.SYNC_LOCK)
		{
			provider.updateElementsListToCache(baseElementList, type);
		}
	}

}
