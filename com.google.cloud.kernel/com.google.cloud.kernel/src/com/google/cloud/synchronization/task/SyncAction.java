package com.google.cloud.synchronization.task;

public enum SyncAction {
	UPDATE_NETWORK_COLLECTION(1),
	UPDATE_STORAGE_COLLECTION(2),
	UPDATE_COMPUTE_COLLECTION(3),
	UPDATE_NETWORK(4),
	UPDATE_STORAGE(5),
	UPDATE_COMPUTE(6),
	COMMIT_NETWORK(7),
	COMMIT_STORAGE(8),
	COMMIT_COMPUTE(9);
	private Integer actionId;
	private SyncAction(int id)
	{
		this.actionId = id;
	}
	public int getActionType()
	{
		return this.actionId;
	}
}
