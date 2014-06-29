package com.google.cloud.synchronization;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.cloud.synchronization.task.SyncTask;

public abstract class SyncWorkerThread extends Thread {
	private boolean cleanList = false;
	private boolean stopWorker = false;
	private static final String TAG = SyncWorkerThread.class.getSimpleName();
	private List<SyncTask> syncQueue = new ArrayList<SyncTask>();

	public SyncWorkerThread() {
		
	}

	public SyncWorkerThread(SyncWorkerPublisher oldInstance) {
		this.syncQueue = new ArrayList<SyncTask>(oldInstance.getSynQueue());
	}

	public void stopWorker(boolean cleanList) {
		this.cleanList = cleanList;
		this.stopWorker = true;
	}

	public void addSyncTask(SyncTask syncTask) {
		synchronized (syncQueue) {
			if (syncTask != null && !getSynQueue().contains(syncTask)) {
				getSynQueue().add(syncTask);
			}
		}
	}

	public void removeSynTasks(int syncEvent) {
		synchronized (syncQueue) {
			if (!getSynQueue().isEmpty()) {
				for (SyncTask syncTask : syncQueue) {
					if (syncTask.getSyncEvent() == syncEvent) {
						getSynQueue().remove(syncTask);
					}
				}
			}
		}
	}

	public List<SyncTask> getSynQueue() {
		return this.syncQueue;
	}

	@Override
	public void run() {
		while(!stopWorker) {
			SyncTask syncTask = null;
			synchronized (syncQueue) {
				if (!getSynQueue().isEmpty()) {
					syncTask = getSynQueue().get(0);
				}
			}
			if (syncTask != null) {
				try {
					handleStartSync();
					syncTask.synchronize();
					synchronized (syncQueue) {
						if (!getSynQueue().isEmpty()) {
							getSynQueue().remove(syncTask);
							handleSuccessSync();
						}
					}
				} catch (Exception e) {
					Log.e(TAG, "Error in running the task." + e.getMessage());
					synchronized (syncQueue) {
						handleFailureSync(syncTask);
					}
				} finally {
					handleFinishedSync();
				}
			}
		}
		cleanUp();
	}

	private void cleanUp() {
		if (cleanList) {
			getSynQueue().clear();
		}
	}

	protected abstract void handleStartSync();

	protected abstract void handleSuccessSync();

	protected abstract void handleFinishedSync();

	protected abstract void handleFailureSync(SyncTask taskFailed);
}
