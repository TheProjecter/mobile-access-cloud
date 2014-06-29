package com.google.cloud.webservice;

import java.io.IOException;
import java.util.List;

import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.ObjectType;

public interface IWebServiceClient {
	/**
	 * Perform a get operation
	 * 
	 * @return
	 * @throws IOException
	 */
	public String get(String loc) throws IOException;

	public String delete(String loc) throws IOException;

	/**
	 * Perform a post operation
	 * 
	 * @return
	 * @throws IOException
	 */
	public String post(String loc, String input) throws IOException;
	
	/**
	 * gets the Network list.
	 * @return the network list.
	 */
	public List<BaseElement> getBaseElementCollections(CollectionType type);
	/**
	 * gets a network by its id.
	 * @param elementId the network element to be retrieved.
	 * @return the network element
	 */
	public BaseElement getBaseElementById(String elementId, ObjectType type);
	/**
	 * updates an object to the server
	 * @param network. object to be updated to the server
	 */
	public void updateBaseElement(BaseElement baseElement);
	/**
	 * creates a new network object on the server
	 * @param network the object to be created.
	 */
	public void createBaseElement(BaseElement baseElement);
	/**
	 * gets a compute element from the server by id.
	 * @param elementId the id of the element to be retrieved.
	 * @return a compute element with the id elementId or null if an object with this id doesn't
	 * exist on the server.
	 */
	public Compute getComputeById(String elementId);
	/** 
	 * post a new compute element to the server.
	 * @param computeToUpdate the compute element to be posted.
	 */
	public void createComputeElement(Compute computeToUpdate);

}