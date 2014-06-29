package com.google.cloud.client.translator;

import java.io.InputStream;
import java.util.List;

import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.po.BaseElementPO;
import com.google.cloud.client.po.ComputePO;
import com.google.cloud.webservice.OperationType;


public interface IContentTranslator {
	/**
	 * gets the xml from the inputstream received from the service.
	 * @param stream the inputstream received
	 * @return the string xml from parsed from the inputstream.
	 * @throws Exception
	 */
	public String getXMLFromInputStream(InputStream stream) throws Exception;
	/**
	 * gets an xml from a current object
	 * @param network the object to be updated
	 * @return the String format from the object format.
	 */
	public String getXMLFromObject(BaseElement baseElement, OperationType type);
	/**
	 * convert a BaseElementPO object to a BaseElement object.
	 * @param baseElementPO the object from which the data is taken.
	 * @return the newly object created from the old object.
	 */
	public BaseElement toBaseElement(BaseElementPO baseElementPO);
	/**
	 * convert a list of BaseElementPO objects to BaseElement objects.
	 * @param baseElementPOList the list of BaseElementPO objects.
	 * @return the newly created BaseElement list of objects.
	 */
	public List<BaseElement> toBaseElementCollections(List<BaseElementPO> baseElementPOList);
	/**
	 * converts a ComputePO Object to a Compute object.
	 * @param computePO the computePO to be converted.
	 * @return the newly converted compute object.
	 */
	public Compute toCompute(ComputePO computePO);
}
