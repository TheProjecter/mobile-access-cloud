package com.google.cloud.provider;

import java.util.List;
import java.util.Random;

import android.util.Log;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.client.objects.AttachedNetwork;
import com.google.cloud.client.objects.AttachedStorage;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.InstanceType;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.client.properties.PropertyName;
import com.google.cloud.client.properties.StringProperty;
import com.google.cloud.synchronization.ISynchronizationManager;

public class DataProvider implements IDataProvider {
	private static final String TAG = DataProvider.class.getSimpleName();
	private static IDataProvider instance = null;

	protected DataProvider() {
	}

	private DataCache getDataCache()
	{
		return CloudKernelApplication.getInstance().getDataCache();
	}
	private IDataFactory getDataFactory()
	{
		return CloudKernelApplication.getInstance().getDataFactory();
	}
	public static IDataProvider getInstance() {
		if (instance == null) {
			instance = new DataProvider();
		}
		return instance;
	}

	public ISynchronizationManager getSyncManager() {
		return CloudKernelApplication.getInstance().getSyncManager();
	}
	
	@Override
	public void initializeData()
	{
		this.getDataCache().clearAll();
		this.getDataFactory().deleteAll();
	}
	@Override
	public List<BaseElement> getCollections(CollectionType type, boolean update) {
		return getElements(update, type);
	}
	private List<BaseElement> getElements(boolean update, CollectionType type)
	{
		if(update)
		{
			getSyncManager().updateBaseElementCollections(type);
		}
		return this.getDataCache().getCollectionByType(type);
	}
	@Override
	public BaseElement getBaseElementById(String elementId, ObjectType type, boolean update) {
		if(update)
		{
			getDataCache().removeElementFromCacheById(elementId);
			getDataFactory().deleteBaseElementById(elementId);
			getSyncManager().updateBaseElement(elementId, type);
		}
		else
		{
			if(getDataCache().containsElementId(elementId))
			{
				return getDataCache().getElementById(elementId);
			}
			else
			{
				return getDataFactory().getElementById(elementId);
			}
		}
		return null;
	}
	@Override
	public Compute getComputeElementById(String elementId, boolean update)
	{
		if(update)
		{
			getDataCache().removeComputeFromCacheById(elementId);
			getDataFactory().deleteComputeById(elementId);
			getSyncManager().updateComputeElement(elementId);
		}
		else
		{
			if(getDataCache().getComputeById(elementId)!=null)
			{
				return getDataCache().getComputeById(elementId);
			}
			else
			{
				return getDataFactory().getComputeById(elementId);
			}
		}
		return null;
	}
	@Override
	public void updateElementsListToCache(List<BaseElement> baseElementList, CollectionType type) {
		//dataFactory.insertNetworks(networkList);
		getDataCache().addBaseElementToList(baseElementList, type);
		for(BaseElement baseElement :  getDataCache().getCollectionByType(type))
		{
			baseElement.update();
		}
	}

	@Override
	public void insertElementToDbIfNotFound(BaseElement newElement) {
		Log.d(TAG, "update a BaseElement object in to the db.");
		if(!getDataCache().containsElement(newElement))
		{
			getDataCache().addBaseElementToCache(newElement);
			getDataFactory().insertElement(newElement);
		}
	}
	
	
	@Override
	public void getAllColectionsFromServer(boolean update) {
		if(update)
		{
			getSyncManager().updateBaseElementCollections(CollectionType.NETWORK_COLLECTIONS);
			getSyncManager().updateBaseElementCollections(CollectionType.STORAGE_COLLECTIONS);
			getSyncManager().updateBaseElementCollections(CollectionType.COMPUTE_COLLECTIONS);
			getSyncManager().updateBaseElementCollections(CollectionType.USER_COLLECTIONS);
			//getSyncManager().updateStorageCollection();
		}
		
	}

	@Override
	public void commitBaseElementToServer(String elementId) {
		if(elementId!=null && getDataCache().getElementById(elementId)!=null)
		{
			BaseElement baseElement = getDataCache().getElementById(elementId);
			if(baseElement!=null)
			{
				getDataFactory().updateElement(baseElement);
			}
			//getSyncManager().updateNetworkToServer(elementId);
		}
	}
	@Override
	public void commitComputeToserver(String elementId) {
		if(elementId!=null && getDataCache().containsComputeId(elementId))
		{
			Compute compute = getDataCache().getComputeById(elementId);
			if(compute!=null)
			{
				getSyncManager().updateComputeToServer(elementId, ObjectType.COMPUTE);
			}
		}
	}
	@Override
	public void insertComputeToDbIfNotExist(Compute compute) {
		Log.d(TAG, "update a Compute object in to the db.");
		if(getDataCache().getComputeById(compute.getElementId())==null)
		{
			getDataCache().addComputeToCache(compute);
			getDataFactory().insertCompute(compute);
		}
	}

	@Override
	public Compute createEmptyCompute() {
		Log.d(TAG, "creating a compute");
		Compute compute = new Compute(String.valueOf(getRandomInt(12)), "compute " + getRandomInt(8));
		compute.setObjectType(ObjectType.COMPUTE.getType());
		return compute;
	}
	private int getRandomInt(int range)
	{
		Random random = new Random();
		int elem = 1 + random.nextInt(range);
		return elem;
	}
	@Override
	public void addPropertiesToCompute(Compute compute)
	{
		StringProperty propertyGroup = new StringProperty(PropertyName.GROUP.getType(), "Default", true);
		StringProperty propertyCpu = new StringProperty(PropertyName.CPU.getType(), "Default", true);
		StringProperty propertyMemory = new StringProperty(PropertyName.MEMORY.getType(), "Default", true);
		StringProperty propertyName = new StringProperty(PropertyName.NAME.getType(), "Default", true);
		compute.addProperty(propertyGroup);
		compute.addProperty(propertyCpu);
		compute.addProperty(propertyMemory);
		compute.addProperty(propertyName);
		AttachedNetwork network = new AttachedNetwork();
		network.setNetworkHref("http://localhost:4567/network/6");
		compute.addNetwork(network);
		AttachedStorage storage = new AttachedStorage();
		storage.setStorageHref("http://localhost:4567/storage/64");
		compute.addStorage(storage);
		InstanceType type = InstanceType.MEDIUM;
		compute.setInstanceType(type);
		getDataCache().addComputeToCache(compute);
	}
}
