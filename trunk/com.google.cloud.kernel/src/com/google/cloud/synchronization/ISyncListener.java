package com.google.cloud.synchronization;

import com.google.cloud.synchronization.task.SyncEvent;

public interface ISyncListener {
	/**
	 * after every sync operation a message should be posted, regarding the 
	 * type of event.
	 * @param update the type of sync.
	 */
	public void receivedSyncNotification(SyncEvent update);
}
