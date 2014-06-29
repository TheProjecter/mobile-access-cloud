package com.google.cloud.synchronization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.google.cloud.synchronization.task.SyncEvent;
import com.google.cloud.synchronization.task.SyncTask;

public class SyncWorkerPublisher extends SyncWorkerThread implements
		ISyncWorkerPublisher {
	private List<ISyncListener> syncListeners = Collections.synchronizedList(new ArrayList<ISyncListener>());
	private static final String TAG = SyncWorkerPublisher.class.getSimpleName();
	public SyncWorkerPublisher() {

	}

	public SyncWorkerPublisher(SyncWorkerPublisher oldInstance) {
		super(oldInstance);
		this.syncListeners.addAll(oldInstance.syncListeners);
	}
	public List<ISyncListener> getSyncListeners()
	{
		return new ArrayList<ISyncListener>(syncListeners);
	}
	@Override
	public void register(ISyncListener listener) {
		if (syncListeners != null) {
			if (listener != null) {
				syncListeners.add(listener);
			}
		}
	}

	@Override
	public void unregister(ISyncListener listener) {
		if (syncListeners != null && !syncListeners.isEmpty()) {
			if (listener != null && syncListeners.contains(listener)) {
				syncListeners.remove(listener);
			}
		}
	}

	@Override
	protected void handleStartSync() {
		for (ISyncListener listener : syncListeners) {
			if (listener != null) {
				listener.receivedSyncNotification(SyncEvent.UPDATE);
			}
		}

	}

	@Override
	protected void handleFinishedSync() {
		for (ISyncListener listener : syncListeners) {
			if (listener != null) {
				listener.receivedSyncNotification(SyncEvent.FINISHED);
			}
		}
	}

	@Override
	protected void handleSuccessSync() {
		for (ISyncListener listener : syncListeners) {
			if (listener != null) {
				listener.receivedSyncNotification(SyncEvent.SUCCESS);
			}
		}
	}
	private void deleteSyncTask(SyncTask task)
	{
		Log.d(TAG, "deleteSyncTask.task failed so we need to remove it.");
		//theoretically we should try to make a remove from our records too.
		//if it's the case obviously.
		getSynQueue().remove(task);
	}
	@Override
	protected void handleFailureSync(SyncTask taskFailed) {
		deleteSyncTask(taskFailed);
		for (ISyncListener listener : syncListeners) {
			if (listener != null) {
				listener.receivedSyncNotification(SyncEvent.SUCCESS);
			}
		}
		
	}
	
}
