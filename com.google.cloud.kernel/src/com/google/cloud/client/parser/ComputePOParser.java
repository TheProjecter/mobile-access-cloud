package com.google.cloud.client.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.client.po.BasePropertyPO;
import com.google.cloud.client.po.ComputePO;
import com.google.cloud.client.po.DiskPO;
import com.google.cloud.client.po.NicPO;
import com.google.cloud.client.properties.PropertyName;

public class ComputePOParser extends DefaultHandler {
	private static final String TAG = ComputePOParser.class.getSimpleName();
	private boolean isId = false;
	private boolean isUser = false;
	private boolean isName = false;
	private boolean isGroup = false;
	private boolean isMemory = false;
	private boolean isCpu = false;
	private boolean isInstanceType = false;
	private boolean isState = false;
	private boolean isStorage = false;
	private boolean isNic = false;
	private boolean isNetwork = false;
	private boolean isMac = false;
	private boolean isIP = false;
	private boolean isType = false;
	private boolean isTarget = false;
	private boolean isDisk = false;
	private boolean isCompute = false;
	private ComputePO computePO = null;
	private DiskPO disk = null;
	private NicPO nic = null;
	private String chars = null;
	private SAXParserFactory parserFactory = null;
	private SAXParser parser = null;
	private static final String href = "href";
	private static final String name = "name";
	private static final String NAME = "NAME";
	private static final String GROUP = "GROUP";
	private static final String STATE = "STATE";
	private static final String DISK = "DISK";
	private static final String NIC = "NIC";
	private static final String CPU = "CPU";
	private static final String TYPE = "TYPE";
	private static final String TARGET = "TARGET";
	private static final String STORAGE = "STORAGE";
	private static final String IP = "IP";
	private static final String INSTANCE_TYPE = "INSTANCE_TYPE";
	private static final String MAC = "MAC";
	private static final String NETWORK = "NETWORK";
	private static final String MEMORY = "MEMORY";
	private static final String USER = "USER";
	private static final String ID = "ID";
	private static final String COMPUTE = "COMPUTE";

	public ComputePOParser() {
		parserFactory = SAXParserFactory.newInstance();
	}

	public void parseCompute(String xml) throws IOException, SAXException,
			ParserConfigurationException {
		if (parserFactory != null) {
			Log.d(TAG, "initializing the parser");
			parser = parserFactory.newSAXParser();
		} else {
			Log.d(TAG, "parserFactory is null");
			return;
		}
		parser.parse(new ByteArrayInputStream(xml.getBytes()), this);
	}

	@Override
	public void startDocument() throws SAXException {
		computePO = new ComputePO();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		Log.d(TAG, "starting the parsing of an element");
		if(localName.equals(COMPUTE))
		{
			isCompute = true;
			computePO.setObjectType(ObjectType.COMPUTE);
			BasePropertyPO propHref = new BasePropertyPO(PropertyName.COMPUTE_HREF.getType(), attributes.getValue(href));
			propHref.setVisible(false);
			computePO.addPropertyToList(propHref);
		}
		else if (localName.equals(ID)) {
			isId = true;
		} else if (localName.equals(USER)) {
			isUser = true;
			BasePropertyPO propPOUserName = new BasePropertyPO(
					PropertyName.USERNAME.getType(),
					attributes.getValue(name));
			BasePropertyPO propPOUserHref = new BasePropertyPO(
					PropertyName.USER_HREF.getType(),
					attributes.getValue(href));
			propPOUserHref.setVisible(false);
			computePO.addPropertyToList(propPOUserHref);
			computePO.addPropertyToList(propPOUserName);
		} else if (localName.equals(GROUP)) {
			this.isGroup = true;
		} else if (localName.equals(CPU)) {
			this.isCpu = true;
		} else if (localName.equals(MEMORY)) {
			this.isMemory = true;
		} else if (localName.equals(NAME)) {
			this.isName = true;
		} else if (localName.equals(INSTANCE_TYPE)) {
			BasePropertyPO propInstanceTypeHref = new BasePropertyPO(
					PropertyName.INSTANCE_TYPE_HREF.getType(),
					attributes.getValue(href));
			propInstanceTypeHref.setVisible(false);
			computePO.addPropertyToList(propInstanceTypeHref);
			String instance_type = parseId(attributes, href);
			BasePropertyPO propInstanceTypeName = new BasePropertyPO(
					PropertyName.INSTANCE_TYPE_NAME.getType(), instance_type);
			computePO.addPropertyToList(propInstanceTypeName);
			this.isInstanceType = true;
		} else if (localName.equals(STATE)) {
			this.isState = true;
		} else if (localName.equals(DISK)) {
			disk = new DiskPO();
			isDisk = true;
		} else if (localName.equals(STORAGE)) {
			disk.setStorageId(parseId(attributes, href));
			disk.setStorageHref(attributes.getValue(href));
			disk.setStorageName(attributes.getValue(name));
			this.isStorage = true;
		} else if (localName.equals(TYPE)) {
			isType = true;
		} else if (localName.equals(TARGET)) {
			isTarget = true;
		} else if (localName.equals(NIC)) {
			nic = new NicPO();
			isNic = true;
		} else if (localName.equals(NETWORK)) {
			nic.setNetworkId(parseId(attributes, href));
			nic.setNetworkHref(attributes.getValue(href));
			nic.setNetworkName(attributes.getValue(name));
			isNetwork = true;
		} else if (localName.equals(MAC)) {
			isMac = true;
		} else if (localName.equals(IP)) {
			isIP = true;
		}
	}

	private String parseId(Attributes attributes, String valueToParse) {
		String idString = "";
		char[] cs = attributes.getValue(valueToParse).toCharArray();
		int length = cs.length;
		int nrOfDigits = 0;
		String ch1 = "/";
		int k = length - 1;
		while (cs[k] != ch1.charAt(0)) {
			nrOfDigits++;
			k--;
		}
		Log.d(TAG, "nr of digits : " + nrOfDigits);
		for (int i = nrOfDigits; i > 0; i--) {
			idString += cs[cs.length - i];
		}
		return idString;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		Log.d(TAG, "ending the parsing of an element");
		if (isId) {
			computePO.setId(chars);
			isId = false;
		}
		else if(isUser)
		{
			isUser = false;
		}
		else if (isGroup) {
			BasePropertyPO propGroup = new BasePropertyPO(
					PropertyName.GROUP.getType(), chars);
			computePO.addPropertyToList(propGroup);
			isGroup = false;
		} else if (isCpu) {
			BasePropertyPO propCpu = new BasePropertyPO(
					PropertyName.CPU.getType(), chars);
			computePO.addPropertyToList(propCpu);
			isCpu = false;
		} else if (isMemory) {
			BasePropertyPO propMemory = new BasePropertyPO(
					PropertyName.MEMORY.getType(), chars);
			computePO.addPropertyToList(propMemory);
			isMemory = false;
		} else if (isName) {
			computePO.setName(chars);
			isName = false;
		} else if (isState) {
			BasePropertyPO propState = new BasePropertyPO(
					PropertyName.STATE.getType(), chars);
			computePO.addPropertyToList(propState);
			isState = false;
		} else if (isStorage) {
			isStorage = false;
		} else if (isType) {
			disk.setType(chars);
			isType = false;
		} else if (isTarget) {
			disk.setTarget(chars);
			isTarget = false;
		} else if (isDisk) {
			computePO.addDiskPO(disk);
			isDisk = false;
		} else if (isNetwork) {
			isNetwork = false;
		} else if (isMac) {
			nic.setMac(chars);
			isMac = false;
		} else if (isIP) {
			nic.setIp(chars);
			isIP = false;
		} else if (isNic) {
			computePO.addNicPO(nic);
			isNic = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String chars_local = new String(ch, start, length);
		this.chars = chars_local.trim();
	}

	public ComputePO getData() {
		return computePO;
	}
}
