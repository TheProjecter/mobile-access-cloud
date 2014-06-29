package com.google.cloud.provider;

import java.util.List;

import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.ObjectType;

public interface IDataProvider {
    /**
     * inserts a BaseElement object received from the server into the database if not found.
     *
     * @param newElement
     */
	public void insertElementToDbIfNotFound(BaseElement newElement);
 
	/**
	 * initial data processing.
	 */
	public void initializeData();
	/**
	 * updates a list of networks received from the server.
	 * @param networkList the list to be updated.
	 */
	public void updateElementsListToCache(List<BaseElement> baseElementList, CollectionType type);
	/**
	 * gets a Network by its id.if the update flag is true, then an update from 
	 * the server will be triggered, otherwise the object will be loaded from
	 * the database.
	 * @param uid the id to be requested.
	 * @param update the flag that should tell from where to get the object.
	 */
	public BaseElement getBaseElementById(String elementId,ObjectType type, boolean update);
	/**
	 * updates all the collections from the server if the update flag is set to true.
	 * @param update true, update from server, false don't update.
	 */
	public void getAllColectionsFromServer(boolean update);
	/**
	 * It tries to update an object based on the android interface modifications.
	 * @param elementId the id of the network to be modified.
	 */
	public void commitBaseElementToServer(String elementId);
	/** gets a list of collections from the server.
	 * 
	 * @param type the type of the collections, e.g {@link NETWORK_COLLECTIONS}, {@link STORAGE_COLLECTIONS}
	 * @param update if set to true it triggers an update from the server, aside from triggering a cache pull.
	 * @return the objects from the cache or null if there are no records.
	 */
	public List<BaseElement> getCollections(CollectionType type, boolean update);
	/**
	 * inserts a compute element to database if it doesn't exist already.
	 * @param compute the new object retrieved from the server.
	 */
	public void insertComputeToDbIfNotExist(Compute compute);
	/**
	 * get a Compute element based on its id.
	 * @param elementId the id of the Compute to be retrieved
	 * @param update true if an update should be made to the server, false if the object
	 * should be retrieved from the local cache or db.
	 * @return the Compute element with the id elementId.
	 */
	public Compute getComputeElementById(String elementId, boolean update);

	public Compute createEmptyCompute();
	/**
	 * commit a compute element to server.
	 * @param elementId
	 */
	public void commitComputeToserver(String elementId);

	public void addPropertiesToCompute(Compute compute);
}
