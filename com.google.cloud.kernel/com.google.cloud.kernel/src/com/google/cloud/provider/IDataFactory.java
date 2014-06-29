package com.google.cloud.provider;

import java.util.List;

import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.User;

public interface IDataFactory {
	/**
	 * deletes a list of networks
	 */
	public void deleteAllNetworks();
	/**
	 * inserts a list of networks
	 * @param networkList the list to be inserted
	 */
	public void insertElements(List<BaseElement> baseElementList);
	
	/**
	 * updates a BaseElement object
	 * @param network the object to be updated.
	 */
	public void updateElement(BaseElement element);
	/**
	 * inserts a new network into the database;
	 * @param network the network to be inserted
	 */
	public void insertElement(BaseElement element);
	/**
	 * inserts an user into the db.
	 * @param user the user to be inserted
	 */
	public void insertUser(User user);
	/**
	 * inserts a list of users into the db.
	 * @param users the list to be inserted
	 */
	public void insertUsers(List<User> users);
	/**
	 * deletes an user.
	 * @param user the user to be deleted
	 */
	public void deleteUser(User user);
	/**
	 * deletes a list of users from the db
	 * @param users the list to be deleted.
	 */
	public void deleteUsers(List<User> users);
	/**
	 * Updates an user.
	 * @param user the user to be updated.
	 */
	public void updateUser(User user);
	/**
	 * Updates a list of users.
	 * @param users the list of users to be updated.
	 */
	public void updateUsers(List<User> users);
	/**
	 * deletes all data from the db.
	 */
	public void deleteAll();
	/**
	 * gets a network by its id.
	 * @param elementId the id of the network to be retrieved
	 * @return an object with the id elementId
	 */
	public BaseElement getElementById(String elementId);
	/**
	 * finds and and deletes an object by its id
	 * @param elementId - the id of the object to be deleted.
	 * @return true if the operation was successful or false otherwise
	 */
	public boolean deleteBaseElementById(String elementId);
	/**
	 * deletes a computes on its id.
	 * @param elementId the id of the compute element to be deleted.
	 */
	public void deleteComputeById(String elementId);
	/**
	 * retrives a compute based on its id.
	 * @param elementId the id of the element to be retrieved.
	 * @return an element with the id elementId or null if not found.
	 */
	public Compute getComputeById(String elementId);
	/**insert a compute into the database.
	 * 
	 * @param compute the object to be inserted.
	 */
	public void insertCompute(Compute compute);
}
