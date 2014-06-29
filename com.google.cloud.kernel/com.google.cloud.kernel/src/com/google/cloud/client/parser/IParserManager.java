package com.google.cloud.client.parser;

import java.util.List;

import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.client.po.BaseElementPO;
import com.google.cloud.client.po.ComputePO;
import com.google.cloud.client.po.NetworkPO;
import com.google.cloud.client.po.StoragePO;

public interface IParserManager {
	/**
	 * maps the xml received from the rest client to classes. The class type should be of
	 * BaseElementPO type. After the BaseElementPO object is created and retrieved the BaseElement object
	 * is constructed.
	 * @param xml the xml to parse.
	 * @return returns the classes out of the xml
	 */
	public BaseElementPO parseBaseElementPO(String xml, ObjectType type);
	/**
	 * maps the xml received via the rest client to classes.
	 * the classes should be instances of the computePO type.
	 * After that from the ComputePO object the Compute object should be constructed.
	 * @param xml the xml to parse
	 * @return returns the classes out of the xml
	 */
	public ComputePO parseComputePO(String xml);
	
	/** 
	 * parse a collection of objects that is not known until runtime. because of the similarities
	 * between the collections it is better to create a parser with the type of collections to
	 * parse as a parameter.
	 * @param xml the xml to parse.
	 * @param type the type of collections to parse.
	 * @return a list of objects from xml.
	 */
	public List<BaseElementPO> parseBaseElementCollections(String xml, CollectionType type);
}
