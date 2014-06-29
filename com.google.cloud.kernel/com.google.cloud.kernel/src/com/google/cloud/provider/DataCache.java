package com.google.cloud.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import android.util.Log;

import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.User;

/**
 * This class is like a filter of the data that exists after the applications is
 * started. it acts like a small flash data for objects that are required. Its
 * purpose is to store collections from the server as they come in order to be
 * able to have up to date collections. After the data collections are stored
 * then a new cache is created for all the objects that had been recorded to he
 * database for which we have information. This can be used if later one wants
 * to enhance the capabilities of the application by supporting the update of
 * objects, instead of delete - insert. Right now when the refresh or update
 * button is triggered everything is deleted from the db and from the cache and
 * the new object instead of being compared to the old object is just inserted.
 * This later can be improved by updating only the properties that really matter
 * or need to be updated.
 * 
 * @author Andrei
 * 
 */
public class DataCache {
	private ConcurrentMap<String, BaseElement> baseElementObjectMap = new ConcurrentHashMap<String, BaseElement>();

	private ConcurrentMap<String, Compute> computeMap = new ConcurrentHashMap<String, Compute>();
	private List<BaseElement> baseElementList = Collections
			.synchronizedList(new ArrayList<BaseElement>());
	private ConcurrentMap<CollectionType, List<BaseElement>> baseElementCollectionsMap = new ConcurrentHashMap<CollectionType, List<BaseElement>>();
	private List<Compute> computeList = Collections
			.synchronizedList(new ArrayList<Compute>());
	private List<User> usersCollections = Collections
			.synchronizedList(new ArrayList<User>());
	private Object cacheLock = new Object();
	private static final String TAG = DataCache.class.getSimpleName();
	private static DataCache instance = null;

	protected DataCache() {

	}

	public static DataCache getInstance() {
		if (instance == null) {
			instance = new DataCache();
		}
		return instance;
	}
	public void addComputeToCache(Compute compute)
	{
		if(compute!=null && computeMap!=null && !computeMap.containsKey(compute.getElementId()))
		{
			computeMap.put(compute.getElementId(), compute);
			compute.update();
		}
		else if(computeMap.containsKey(compute.getElementId()))
		{
			Compute toUpdate = computeMap.get(compute.getElementId());
			if(!toUpdate.equals(compute))
			{
				computeMap.put(compute.getElementId(), compute);
			}
		}
	}
	
	public void addBaseElementToCache(BaseElement baseElement) {
		if (baseElement != null && 
			baseElementObjectMap != null && 
			!baseElementObjectMap.containsKey(baseElement.getElementId())) 
		{
			baseElementObjectMap.put(baseElement.getElementId(), baseElement);
			Log.d(TAG, "addBaseElementToCache.object.received.updat");
			baseElement.update();
		} else if (baseElementObjectMap.containsKey(baseElement.getElementId())) {
			Log.d(TAG, "updating the object");
			BaseElement toUpdate = baseElementObjectMap.get(baseElement.getElementId());
			if (!toUpdate.equals(baseElement)) {
				baseElementObjectMap.put(baseElement.getElementId(),
						baseElement);
			}
		}
	}

	public void removeElementFromCache(BaseElement baseElement) {
		if (baseElement != null && baseElementObjectMap != null
				&& !baseElementObjectMap.isEmpty()) {
			if (baseElementObjectMap.containsValue(baseElement)) {
				baseElementObjectMap.remove(baseElement);
			}
		}
	}
	public void removeComputeFromCache(Compute compute)
	{
		if(compute!=null && computeMap!=null && !computeMap.isEmpty())
		{
			if(computeMap.containsValue(compute))
			{
				computeMap.remove(compute);
			}
		}
	}
	public void removeComputeFromCacheById(String elementId)
	{
		if(elementId!=null && !elementId.equals(""))
		{
			if(computeMap.containsKey(elementId))
			{
				computeMap.remove(elementId);
			}
		}
	}
	public void removeElementFromCacheById(String elementId) {
		if (elementId != null && !elementId.equals("")) {
			if (baseElementObjectMap.containsKey(elementId)) {
				baseElementObjectMap.remove(elementId);
			}
		}
	}
	public void removeComputeFromCollection(Compute compute)
	{
		if(compute!=null && computeList!=null && !computeList.isEmpty())
		{
			if(computeList.contains(compute))
			{
				computeList.remove(compute);
			}
		}
	}
	public void removeBaseElementFromCollection(BaseElement baseElement) {
		if (baseElement != null && baseElementList != null
				&& !baseElementList.isEmpty()) {
			if (baseElementList.contains(baseElement)) {
				baseElementList.remove(baseElement);
			}
		}
	}

	public void addBaseElementToList(List<BaseElement> list,
			CollectionType type) {
		try {
			for (BaseElement baseElement : list) {
				if (baseElement != null) {
					if (!this.baseElementList.contains(baseElement)) {
						baseElementList.add(baseElement);
					}
				}
			}
			baseElementCollectionsMap.put(type, list);
		} catch (Exception e) {
			Log.e(TAG, "Exception in adding data to the list." + e.getMessage());
		}
	}

	public List<BaseElement> getBaseElementList() {
		return baseElementList;
	}

	public Map<CollectionType, List<BaseElement>> getBaseElementCollectionsMap() {
		return baseElementCollectionsMap;
	}

	public List<BaseElement> getCollectionByType(CollectionType type) {
		return baseElementCollectionsMap.get(type);
	}
	public void clearAll() {
		if (baseElementObjectMap != null && !baseElementObjectMap.isEmpty()) {
			baseElementObjectMap.clear();
		}
		if (computeMap != null && !computeMap.isEmpty()) {
			computeMap.clear();
		}
		if (baseElementList != null && !baseElementList.isEmpty()) {
			baseElementList.clear();
		}
		if (computeList != null && !computeList.isEmpty()) {
			computeList.clear();
		}
	}
	public boolean containsElementId(String elementId)
	{
		if(elementId!=null && !elementId.equals(""))
		{
			if(baseElementObjectMap.containsKey(elementId))
			{
				return true;
			}
		}
		return false;
	}
	public boolean containsElement(BaseElement element) {
		if (element != null && baseElementObjectMap.containsValue(element)) {
			return true;
		}
		return false;
	}
   
	public boolean containsComputeId(String elementId)
	{
		if(elementId!=null && !elementId.equals(""))
		{
			if(computeMap.containsKey(elementId))
			{
				return true;
			}
		}
		return false;
	}
	public boolean containsCompute(Compute element) {
		if (element != null && computeMap.containsValue(element)) {
			return true;
		}
		return false;
	}
	
	public List<Compute> getComputeCollections() {
		return computeList;
	}

	public Compute getComputeById(String elementId) {
		return computeMap.get(elementId);
	}

	public BaseElement getElementById(String elementId) {
		if(elementId!=null && baseElementObjectMap!=null)
		{
			return baseElementObjectMap.get(elementId);
		}
		return null;
	}

}
