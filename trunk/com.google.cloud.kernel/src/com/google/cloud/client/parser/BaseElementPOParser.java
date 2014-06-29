package com.google.cloud.client.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.client.po.BaseElementPO;
import com.google.cloud.client.po.BasePropertyPO;
import com.google.cloud.client.properties.PropertyName;

public class BaseElementPOParser extends DefaultHandler {

	private boolean isId = false;
	private boolean isUser = false;
	private boolean isName = false;
	private boolean isGroup = false;
	private boolean isState = false;
	private boolean isType = false;
	private boolean isAddress = false;
	private boolean isSize = false;
	private boolean isPublic = false;
	private boolean isNetwork = false;
	private boolean isStorage = false;
	private boolean isFsType = false;
	private boolean isPersistent = false;

	private BaseElementPO element;
	private SAXParserFactory parserFactory = null;
	private SAXParser parser = null;
	private static final String href = "href";
	private static final String name = "name";
	private static final String NAME = "NAME";
	private static final String ADDRESS = "ADDRESS";
	private static final String SIZE = "SIZE";
	private static final String GROUP = "GROUP";
	private static final String ID = "ID";
	private static final String USER = "USER";
	private static final String STATE = "STATE";
	private static final String USER_ID = "USER_ID";
	private static final String PERSISTENT = "PERSISTENT";
	private static final String FS_TYPE = "FSTYPE";
	private static final String TYPE = "TYPE";
	private static final String ISPUBLIC = "PUBLIC";
	private static final String NETWORK = "NETWORK";
	private static final String STORAGE = "STORAGE";
	private final ObjectType objType;

	public BaseElementPOParser(ObjectType type) {
		this.objType = type;
		parserFactory = SAXParserFactory.newInstance();
	}

	/**
	 * to do. think about parsing the inputstream directly without using the
	 * content translator. it might work.
	 * 
	 * @param xml
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public void parseNetwork(String xml) throws IOException, SAXException,
			ParserConfigurationException {
		if (parserFactory != null) {
			parser = parserFactory.newSAXParser();
		} else {
			return;
		}
		parser.parse(new ByteArrayInputStream(xml.getBytes()), this);
	}

	@Override
	public void startDocument() throws SAXException {
		element = new BaseElementPO();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (objType == ObjectType.NETWORK) {
			if (localName.equals(NETWORK)) {
				isNetwork = true;
				element.setObjectType(ObjectType.NETWORK);
				BasePropertyPO propHref = new BasePropertyPO(PropertyName.NETWORK_HREF.getType(),
						attributes.getValue(href));
				propHref.setVisible(false);
				element.addPropertyToList(propHref);
			} else if (localName.equals(ID)) {
				isId = true;
			} else if (localName.equals(USER)) {
				isUser = true;
				BasePropertyPO propUser = new BasePropertyPO(PropertyName.USERNAME.getType(),
						attributes.getValue(name));
				element.addPropertyToList(propUser);
				char[] cs = attributes.getValue(href).toCharArray();
				char last = cs[cs.length - 1];
				String idString = String.valueOf(last);
				BasePropertyPO propUserId = new BasePropertyPO(PropertyName.USERID.getType(),
						idString);
				element.addPropertyToList(propUserId);
			} else if (localName.equals(NAME)) {
				isName = true;
			} else if (localName.equals(GROUP)) {
				isGroup = true;
			} else if (localName.equals(ADDRESS)) {
				isAddress = true;
			} else if (localName.equals(SIZE)) {
				isSize = true;
			} else if (localName.equals(ISPUBLIC)) {
				isPublic = true;
			}
		} else if (objType == ObjectType.STORAGE) {
			if (localName.equals(STORAGE)) {
				isStorage = true;
				element.setObjectType(ObjectType.STORAGE);
				BasePropertyPO propHref = new BasePropertyPO(PropertyName.STORAGE_HREF.getType(),
						attributes.getValue(href));
				propHref.setVisible(false);
				element.addPropertyToList(propHref);
			} else if (localName.equals(ID)) {
				isId = true;
			} else if (localName.equals(USER)) {
				isUser = true;
				BasePropertyPO propUserName = new BasePropertyPO(PropertyName.USERNAME.getType(),
						attributes.getValue(name));
				element.addPropertyToList(propUserName);
				char[] cs = attributes.getValue(href).toCharArray();
				char last = cs[cs.length - 1];
				String idString = String.valueOf(last);
				BasePropertyPO propUserId = new BasePropertyPO(PropertyName.USERID.getType(),
						idString);
				element.addPropertyToList(propUserId);
			} else if (localName.equals(NAME)) {
				isName = true;
			} else if (localName.equals(GROUP)) {
				isGroup = true;
			} else if (localName.equals(STATE)) {
				isState = true;
			} else if (localName.equals(TYPE)) {
				isType = true;
			} else if (localName.equals(SIZE)) {
				isSize = true;
			} else if (localName.equals(FS_TYPE)) {
				isFsType = true;
			} else if (localName.equals(ISPUBLIC)) {
				isPublic = true;
			} else if (localName.equals(PERSISTENT)) {
				isPersistent = true;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (objType == ObjectType.NETWORK) {
			if (isNetwork) {
				isNetwork = false;
			} else if (isId) {
				isId = false;
			} else if (isUser) {
				isUser = false;
			} else if (isName) {
				isName = false;
			} else if (isGroup) {
				isGroup = false;
			} else if (isAddress) {
				isAddress = false;
			} else if (isSize) {
				isSize = false;
			} else if (isPublic) {
				isPublic = false;
			}
		} else if (objType == ObjectType.STORAGE) {
			if (isStorage) {
				isStorage = false;
			} else if (isId) {
				isId = false;
			} else if (isUser) {
				isUser = false;
			} else if (isName) {
				isName = false;
			} else if (isGroup) {
				isGroup = true;
			} else if (isState) {
				isState = true;
			} else if (isType) {
				isType = true;
			} else if (isSize) {
				isSize = true;
			} else if (isFsType) {
				isFsType = true;
			} else if (isPublic) {
				isPublic = true;
			} else if (isPersistent) {
				isPersistent = true;
			}

		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String chars = new String(ch, start, length);
		chars = chars.trim();
		if (objType == ObjectType.NETWORK) {
			if (isNetwork) {
				isNetwork = false;
			}
			if (isId) {
				element.setId(chars);
				isId = false;
			} else if (isName) {
				element.setName(chars);
				isName = false;
			} else if (isGroup) {
				BasePropertyPO propGroupPO = new BasePropertyPO(PropertyName.GROUP.getType(), chars);
				element.addPropertyToList(propGroupPO);
				isGroup = false;
			} else if (isAddress) {
				BasePropertyPO propAddress = new BasePropertyPO(PropertyName.ADDRESS.getType(), chars);
				element.addPropertyToList(propAddress);
				isAddress = false;
			} else if (isSize) {
				BasePropertyPO propSize = new BasePropertyPO(PropertyName.SIZE.getType(), chars);
				element.addPropertyToList(propSize);
				isSize = false;
			} else if (isPublic) {
				BasePropertyPO propPublicElem = new BasePropertyPO(PropertyName.PUBLIC.getType(),
						chars);
				element.addPropertyToList(propPublicElem);
				isPublic = false;
			}
		} else if (objType == ObjectType.STORAGE) {
			if (isStorage) {
				isStorage = false;
			}
			if (isId) {
				element.setId(chars);
				isId = false;
			} else if (isName) {
				element.setName(chars);
				isName = false;
			} else if (isGroup) {
				BasePropertyPO propGroup = new BasePropertyPO(PropertyName.GROUP.getType(), chars);
				element.addPropertyToList(propGroup);
				isGroup = false;
			} else if (isState) {
				BasePropertyPO propState = new BasePropertyPO(PropertyName.STATE.getType(), chars);
				element.addPropertyToList(propState);
				isState = false;
			} else if (isType) {
				BasePropertyPO propType = new BasePropertyPO(PropertyName.STORAGE_TYPE.getType(), chars);
				element.addPropertyToList(propType);
				isType = false;
			} else if (isSize) {
				BasePropertyPO propSize = new BasePropertyPO(PropertyName.SIZE.getType(), chars);
				element.addPropertyToList(propSize);
				isSize = false;
			} else if (isFsType) {
				BasePropertyPO propFsType = new BasePropertyPO(PropertyName.STORAGE_FSTYPE.getType(), chars);
				element.addPropertyToList(propFsType);
				isFsType = false;
			} else if (isPublic) {
				BasePropertyPO propPublicElement = new BasePropertyPO(PropertyName.PUBLIC.getType(),
						chars);
				element.addPropertyToList(propPublicElement);
				isPublic = false;
			} else if (isPersistent) {
				BasePropertyPO propPersistent = new BasePropertyPO(PropertyName.STORAGE_PERSISTENT.getType(),
						chars);
				element.addPropertyToList(propPersistent);
				isPersistent = false;
			}
		}
	}

	public BaseElementPO getData() {
		return element;
	}

}
