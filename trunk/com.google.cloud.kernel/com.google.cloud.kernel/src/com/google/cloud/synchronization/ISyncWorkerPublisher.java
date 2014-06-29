package com.google.cloud.synchronization;

public interface ISyncWorkerPublisher {
	/**
	 * registers listeners that listens to updates.
	 * @param listener the listener to be registred
	 */
	public void register(ISyncListener listener);
	/**
	 * unregisters listeners
	 * @param listener the listener to be unregistered.
	 */
	public void unregister(ISyncListener listener);
}
