package com.google.cloud.synchronization;

import android.util.Log;

import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.synchronization.task.CommitComputeTaskSync;
import com.google.cloud.synchronization.task.CommitNetworkSync;
import com.google.cloud.synchronization.task.SyncTask;
import com.google.cloud.synchronization.task.UpdateBaseElementCollectionTask;
import com.google.cloud.synchronization.task.UpdateBaseElement;
import com.google.cloud.synchronization.task.UpdateStorageCollectionTask;

public class SynchronizationManager implements ISynchronizationManager {

	private static ISynchronizationManager instance = null;
	private SyncWorkerPublisher syncThread = null;
	private static final String TAG = SynchronizationManager.class
			.getSimpleName();

	protected SynchronizationManager() {
		syncThread = new SyncWorkerPublisher();
		// createThread();
	}

	public static ISynchronizationManager getInstance() {
		if (instance == null) {
			instance = new SynchronizationManager();
		}
		return instance;
	}

	@Override
	public void createThread() {
		syncThread = new SyncWorkerPublisher();
		syncThread.setPriority(3);
	}
	private SyncWorkerPublisher createThread(SyncWorkerPublisher oldInstance) {
		SyncWorkerPublisher newSyncThread = new SyncWorkerPublisher(oldInstance);
		newSyncThread.setPriority(3);
		return newSyncThread;
	}
	@Override
	public void startThread() {
		try {
			if (syncThread == null) {
				createThread();
			}
			switch (syncThread.getState()) {
			case NEW:
				syncThread.start();
				break;
			case BLOCKED:
			case WAITING:
			case RUNNABLE:
			case TIMED_WAITING:
				Log.d(TAG, "Stopping the thread");
				stopThread();
				break;
			case TERMINATED:
				syncThread = createThread(syncThread);
				syncThread.start();
				break;
			default:
				stopThread();
				startThread();
				break;
			}
		} catch (IllegalThreadStateException e) {
			Log.e(TAG, "Exceptin starting the worker thread.");
		}

	}


	@Override
	public void stopThread() {
		try {
			if (syncThread != null && syncThread.isAlive()) {
				Log.w(TAG, "stopping the thread");
				syncThread.stopWorker(true);
				syncThread.join();
			}
		} catch (InterruptedException e) {
			Log.e(TAG, "Interrupted exception.message" + e.getMessage());
		}

	}

	private ISyncWorkerPublisher getSyncThread()
	{
		return syncThread;
	}
	@Override
	public ISyncWorkerPublisher getSyncWorkerPublisher()
	{
		return getSyncThread();
	}
	
	@Override
	public void updateBaseElementCollections(CollectionType type) {
		UpdateBaseElementCollectionTask baseElementsCollectionTask = new UpdateBaseElementCollectionTask(type);
		addSyncTask(baseElementsCollectionTask);
	}
	protected void addSyncTask(SyncTask task)
	{
		if(syncThread!=null)
		{
			syncThread.addSyncTask(task);
		}
	}
	@Override
	public void updateBaseElement(String elementId, ObjectType type) {
		UpdateBaseElement networkSync = new UpdateBaseElement(elementId, type);
		addSyncTask(networkSync);
	}
	
	@Override
	public void commitBaseElementToServer(String elementId, ObjectType type) {
		CommitNetworkSync networkCommit = new CommitNetworkSync(elementId, type);
		addSyncTask(networkCommit);
	}

	@Override
	public void updateStorageToServer(String elementId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateComputeElement(String elementId) {
		UpdateComputeSync computeTask = new UpdateComputeSync(elementId);
		addSyncTask(computeTask);
	}

	@Override
	public void updateComputeToServer(String elementId, ObjectType type) {
		CommitComputeTaskSync computeTask = new CommitComputeTaskSync(elementId, type);
		addSyncTask(computeTask);
	}
}
