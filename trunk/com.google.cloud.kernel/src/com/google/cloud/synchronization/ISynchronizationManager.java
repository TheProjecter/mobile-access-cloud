package com.google.cloud.synchronization;

import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.objects.ObjectType;

public interface ISynchronizationManager 
{
	/**
	 * updates the networks list from the current database
	 */
	public void updateBaseElementCollections(CollectionType type);
	/**
	 * after the thread instance is create the thread is started
	 * @throws Exception 
	 */
	public void startThread();
	/**
	 * stops the process of the thread
	 * @throws Exception
	 */
	public void stopThread();
	/**
	 * creates the thread that does the job.
	 */
	public void createThread();
	/**
	 * updates a network by its id. if the network it's in the local db.
	 *.If it is not in the local db an update with the current id will be made to the server. 
	 * @param elementId
	 */
	public void updateBaseElement(String elementId, ObjectType type);
	/**
	 * gets the SyncWorkerpublisher to be able to register listeners for receiving
	 * updates regarding the status of the sync.
	 * @return the current worker thread.
	 */
	public ISyncWorkerPublisher getSyncWorkerPublisher();
	/**
	 * updates a network which has the id elementId.
	 * @param elementId the id of the network to be updated to the server.
	 */
	public void commitBaseElementToServer(String elementId, ObjectType type);
	/**
	 * updates a storage which has the id elementId.
	 * @param elementId the id of the storage to be updated to the server.
	 */
	public void updateStorageToServer(String elementId);	
	/**
	 * update a compute element based on its id.
	 * @param elementId the id of the element to be updated.
	 */
	public void updateComputeElement(String elementId);
	/**
	 * update a compute to server based on its id.
	 * @param elementId the id of the compute to update.
	 */
	public void updateComputeToServer(String elementId, ObjectType type);
}
