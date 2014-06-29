package com.google.cloud.synchronization.task;

import java.util.UUID;



public abstract class SyncTask {
	private Integer syncEvent;
	private String elementId;
	protected static final String TAG = SyncTask.class.getSimpleName();
	public static final Object SYNC_LOCK = new Object();
	/**
	 * the purpose of this is to just create unique SyncTask objects.
	 */
	private UUID uuidTask = null;
	public SyncTask(int syncEvent, String elementId) {
		this.syncEvent = syncEvent;
		this.elementId = elementId;
		this.uuidTask = UUID.randomUUID();
	}

	public Integer getSyncEvent() {
		return this.syncEvent;
	}
	public String getElementId()
	{
		return elementId;
	}
	@Override
	public int hashCode() {
		int prim = 29;
		long sum = prim * syncEvent + prim - 4;
		return (int) sum;
	}
	public UUID getUUID()
	{
		return this.uuidTask;
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof SyncTask) {
			return internalEquals((SyncTask) o);
		}
		return super.equals(o);
	}
	
	private boolean internalEquals(SyncTask other) {
		if (other != null) 
		{
			if (this.getSyncEvent() != null) 
			{
				if (other.getSyncEvent() == null) 
				{
					return false;
				}
				else if (!this.getSyncEvent().equals(other.getSyncEvent())) 
				{
					return false;
				}
			}
			if(this.getUUID()!=null) 
			{
				if(other.getUUID()==null)
				{
					return false;
				}
				else if(!this.getUUID().equals(other.getUUID()))
				{
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}

	public abstract void synchronize() throws Exception;
}
