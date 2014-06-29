package com.google.cloud.synchronization.task;

import android.util.Log;


public class UpdateStorageCollectionTask extends SyncTask {

	public UpdateStorageCollectionTask(int syncEvent) {
		super(syncEvent, null);
	}
	public UpdateStorageCollectionTask()
	{
		this(SyncAction.UPDATE_STORAGE.getActionType());
	}
	@Override
	public void synchronize() throws Exception 
	{
		Log.d(TAG, "update a collection of storages");
		synchronized (SyncTask.SYNC_LOCK) 
		{
			
		}
	}

}
