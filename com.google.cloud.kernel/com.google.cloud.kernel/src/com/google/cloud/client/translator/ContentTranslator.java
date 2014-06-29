package com.google.cloud.client.translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.util.Log;

import com.google.cloud.client.objects.AttachedNetwork;
import com.google.cloud.client.objects.AttachedStorage;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.InstanceType;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.client.po.BaseElementPO;
import com.google.cloud.client.po.BasePropertyPO;
import com.google.cloud.client.po.ComputePO;
import com.google.cloud.client.po.DiskPO;
import com.google.cloud.client.po.NicPO;
import com.google.cloud.client.properties.PropertyName;
import com.google.cloud.client.properties.StringProperty;
import com.google.cloud.webservice.OperationType;

/**
 * class created for the purpose of translating from InputStream to xml and later on for translating from xmls 
 * objects.
 * @author Andrei
 *
 */
public class ContentTranslator implements IContentTranslator {
	private static IContentTranslator instance = null;
	private static final String TAG = ContentTranslator.class.getSimpleName();

	protected ContentTranslator() {

	}

	public static IContentTranslator getInstance() {
		if (instance == null) {
			instance = new ContentTranslator();
		}
		return instance;
	}
	@Override
	public String getXMLFromInputStream(InputStream stream) throws Exception
	{
		Exception ex = null;
		try 
		{
			//DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//DocumentBuilder db = factory.newDocumentBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			StringBuilder builder = new StringBuilder();
			String line = "";
			while((line=reader.readLine())!=null)
			{
				builder.append(line);
			}
			Log.d(TAG, "the string is : " + builder.toString());
			return builder.toString();
		} catch (Exception e) {
			if(e instanceof IOException)
			{
				ex = new IOException("IOException in parsing the xml");
				Log.e(TAG, "IOException in parsing the xml : " + e.getMessage());
			}
			else if(e instanceof SAXException)
			{
				ex = new SAXException("SAXException with the message" + e.getMessage());
				Log.e(TAG, "SAXException with the message : " + e.getMessage());
			}
			else if(e instanceof ParserConfigurationException)
			{
				ex = new ParserConfigurationException("ParserConfigurationException in parsing the xml");
				Log.e(TAG, "ParserConfigurationException with the message : " + e.getMessage());
			}
		}
		if(ex!=null)
		{
			throw ex;
		}
		return null;
	
	}
	@Override
	public BaseElement toBaseElement(BaseElementPO baseElementPO)
	{
		return toBaseElementInternal(baseElementPO);
	}
	private BaseElement toBaseElementInternal(BaseElementPO baseElementPO)
	{
		String baseElementId = baseElementPO.getId();
		BaseElement baseElement = new BaseElement(baseElementId,
												  baseElementPO.getObjectType().getType(), 
												  baseElementPO.getName());
		for(BasePropertyPO propPO : baseElementPO.getPropertyList()){
			StringProperty prop = new StringProperty(propPO.getKey(), propPO.getValue());
			prop.setVisible(propPO.getIsVisible());
			baseElement.addProperty(prop);
		}
		return baseElement;
	}
	@Override
	public Compute toCompute(ComputePO computePO) {
		return toComputeInternal(computePO);
	}
	private Compute toComputeInternal(ComputePO computePO)
	{
		Compute compute = null;
		String id = computePO.getId();
		compute = new Compute(id, computePO.getName());
		for(DiskPO diskPO : computePO.getDiskList())
		{
			AttachedStorage disk  = new AttachedStorage();
			disk.setStorageHref(diskPO.getStorageHref());
			disk.setStorageName(diskPO.getStorageName());
			disk.setStorageId(diskPO.getStorageId());
			disk.setType(diskPO.getType());
			disk.setTarget(diskPO.getTarget());
			compute.addStorage(disk);
		}
		for(NicPO nicPO : computePO.getNicList())
		{
			AttachedNetwork network = new AttachedNetwork();
			network.setNetworkHref(nicPO.getNetworkHref());
			network.setNetworkName(nicPO.getNetworkName());
			network.setNetworkId(nicPO.getNetworkId());
			network.setIp(nicPO.getIp());
			network.setMac(nicPO.getMac());
			compute.addNetwork(network);
		}
		for(BasePropertyPO propPO : computePO.getPropertyList())
		{
			StringProperty prop = new StringProperty(propPO.getKey(), propPO.getValue());
			prop.setVisible(propPO.getIsVisible());
			compute.addProperty(prop);
		}
		return compute;
	}
	@Override
	public List<BaseElement> toBaseElementCollections(List<BaseElementPO> baseElementPOList)
	{
		List<BaseElement> baseElementList = new ArrayList<BaseElement>();
		for(BaseElementPO baseElementPO : baseElementPOList)
		{
			baseElementList.add(toBaseElementInternal(baseElementPO));
		}
		return baseElementList;
	}

	@Override
	public String getXMLFromObject(BaseElement baseElement, OperationType type) {
		if(baseElement==null)
		{
			return "";
		}
		if(baseElement.getObjectType().equals(ObjectType.NETWORK.getType()))
		{
			return getXMLFromNetworkObject(baseElement, type);
		}
		else if(baseElement.getObjectType().equals(ObjectType.STORAGE.getType()))
		{
			return getXMLFromStorageObject(baseElement, type);
		}
		else if(baseElement.getObjectType().equals(ObjectType.COMPUTE.getType()))
		{
			return getXMLFromComputeObject((Compute)baseElement, type);
		}
		return null;
	}
	private String getXMLFromComputeObject(Compute compute, OperationType operation)
	{
		StringBuffer xml = new StringBuffer();
		if(operation==OperationType.UPDATE)
		{
			xml.append("<COMPUTE href = \"");
			String href = compute.getPropertyByKey(PropertyName.COMPUTE_HREF.getType()).getValue();
			xml.append(href);
			xml.append("\" >");
		}
		else if(operation==OperationType.CREATE)
		{
			xml.append("<COMPUTE>");
		}
		xml.append("<ID>");
		xml.append("" + compute.getElementId());
		xml.append("</ID>");
		StringProperty propertyName = compute.getPropertyByKey(PropertyName.NAME.getType());
		if(propertyName!=null)
		{
			xml.append("<NAME>");
			xml.append(propertyName.getValue());
			xml.append("</NAME>");
		}
		InstanceType type = compute.getInstanceType();
		if(type.getType()!=null && !type.getType().equals(""))
		{
			xml.append("<INSTANCE_TYPE>");
			xml.append(type.getType());
			xml.append("</INSTANCE_TYPE>");
		}
		StringProperty propertyGroup = compute.getPropertyByKey(PropertyName.GROUP.getType());
		if(propertyGroup!=null)
		{
			xml.append("<GROUP>");
			xml.append("" + propertyGroup.getValue());
			xml.append("</GROUP>");
		}
		StringProperty propertyMemory = compute.getPropertyByKey(PropertyName.MEMORY.getType());
		if(propertyMemory!=null)
		{
			xml.append("<MEMORY>");
			xml.append(propertyMemory.getValue());
			xml.append("</MEMORY>");
		}
		StringProperty propertyCpu = compute.getPropertyByKey(PropertyName.CPU.getType());
		if(propertyCpu!=null)
		{
			xml.append("<CPU>");
			xml.append(propertyCpu.getValue());
			xml.append("</CPU>");
		}
		List<AttachedNetwork> networks = compute.getAttachedNetworks();
		List<AttachedStorage> storages = compute.getAttachedStorages();
		for(AttachedStorage storage : storages)
		{
			xml.append("<DISK>");
			xml.append("<STORAGE href = \"");
			String href = storage.getStorageHref();
			xml.append(href);
			xml.append("\" >");
			xml.append("</STORAGE>");
			xml.append("</DISK>");
		}
		for(AttachedNetwork  network : networks)
		{
			xml.append("<NIC>");
			xml.append("<NETWORK href = \"");
			String href = network.getNetworkHref();
			xml.append(href);
			xml.append("\" >");
			xml.append("</NETWORK>");
			xml.append("</NIC>");
		}
		xml.append("</COMPUTE>");
		return xml.toString();
	}
	private String getXMLFromStorageObject(BaseElement baseElement,
			OperationType type) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getXMLFromNetworkObject(BaseElement network,OperationType type)
	{
		StringBuffer xml = new StringBuffer();
		if(type==OperationType.UPDATE)
		{
			xml.append("<NETWORK href = \"");
			String href = network.getPropertyByKey(PropertyName.HREF.getType()).getValue();
			xml.append(href);
			xml.append("\" >");
		}
		else if(type==OperationType.CREATE)
		{
			xml.append("<NETWORK>");
		}
		xml.append("<ID>");
		xml.append("" + network.getElementId());
		xml.append("</ID>");
		xml.append("<NAME>");
		xml.append(network.getName());
		xml.append("</NAME>");
		StringProperty addressProperty = network.getPropertyByKey(PropertyName.ADDRESS.getType());
		if(addressProperty!=null)
		{
			xml.append("<ADDRESS>");
			xml.append("" + addressProperty.getValue());
			xml.append("</ADDRESS>");
		}
		StringProperty sizeProp = network.getPropertyByKey(PropertyName.SIZE.getType());
		if(sizeProp!=null)
		{
			xml.append("<SIZE>");
			xml.append(sizeProp.getValue());
			xml.append("</SIZE>");
		}
		xml.append("</NETWORK>");
		return xml.toString();
	}

	

}
