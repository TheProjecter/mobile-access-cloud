package com.google.cloud.client.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.cloud.client.po.StoragePO;

public class StoragePOParser extends DefaultHandler {
	private boolean isId = false;
	private boolean isUser = false;
	private boolean isName = false;
	private boolean isGroup = false;
	private boolean isType = false;
	private boolean isSize = false;
	private boolean isDescription = false;
	private StoragePO storagePO = null;
	private String chars = null;
	private SAXParserFactory parserFactory = null;
	private SAXParser parser = null;
	public StoragePOParser() {	
		parserFactory = SAXParserFactory.newInstance();
	}

	@Override
	public void startDocument() throws SAXException {
		this.storagePO = new StoragePO();
	}
	public void parseStorage(String xml) throws IOException,SAXException,ParserConfigurationException
	{
		if(parserFactory!=null)
		{
			parser = parserFactory.newSAXParser();
		}
		else 
		{
			return;
		}
		parser.parse(new ByteArrayInputStream(xml.getBytes()), this);
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals("ID")) {
			isId = true;
		} else if (localName.equals("USER")) {
			isUser = true;
			storagePO.setUserName(attributes.getValue("name"));
			isUser = true;
			storagePO.setUserName(attributes.getValue("name"));
			int length = attributes.getValue("href").length();
			String idString = new String(attributes.getValue("href").toCharArray(),
										 length-2,
										 length-1);
			storagePO.setUserId(idString);
		} else if (localName.equals("NAME")) {
			this.isName = true;
		} else if (localName.equals("GROUP")) {
			this.isGroup = true;
		} else if (localName.equals("DESCRIPTION")) {
			this.isDescription = true;
		} else if (localName.equals("TYPE")) {
			this.isType = true;
		} else if (localName.equals("SIZE")) {
			this.isSize = true;
		}
		super.startElement(uri, localName, qName, attributes);
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (isId) {
			storagePO.setId(chars);
			isId = false;
		} else if (isUser) {
			isUser = false;
		} else if (isName) {
			storagePO.setName(chars);
			isName = false;
		} else if (isType) {
			storagePO.setType(chars);
			isType = false;
		} else if (isDescription) {
			storagePO.setDescription(chars);
			isDescription = false;
		} else if (isSize) {
			storagePO.setSize(chars);
			isSize = false;
		} else if (isGroup) {
			storagePO.setGroup(chars);
			isGroup = false;
		}
	};

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String chars_local = new String(ch, start, length);
		this.chars = chars_local.trim();
	}

	public StoragePO getData() {
		return storagePO;
	}

	
}
