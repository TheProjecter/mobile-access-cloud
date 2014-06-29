package com.google.cloud.client.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.client.po.BaseElementPO;

public class BaseElementCollectionsParser extends DefaultHandler {

	private boolean isBaseElement = false;
	private List<BaseElementPO> baseElementCollection;
	private SAXParserFactory parserFactory = null;
	private SAXParser parser = null;
	private static final String network = "NETWORK";
	private static final String storage = "STORAGE";
	private static final String compute = "COMPUTE";
	private static final String user = "USER";
	private static final String href = "href";
	private static final String name = "name";
	private static final String TAG = BaseElementCollectionsParser.class.getSimpleName();
	private CollectionType collectionType;

	public BaseElementCollectionsParser(CollectionType type) {
		parserFactory = SAXParserFactory.newInstance();
		this.collectionType = type;
	}

	public void parseBaseElementList(String xml) throws IOException,
			SAXException, ParserConfigurationException {
		if (parserFactory != null) {
			parser = parserFactory.newSAXParser();
		} else {
			return;
		}
		parser.parse(new ByteArrayInputStream(xml.getBytes()), this);
	}

	@Override
	public void startDocument() throws SAXException {
		baseElementCollection = new ArrayList<BaseElementPO>();
	}
	/*
	 *	 the reason for taking just the id is that the only difference
	 is the id and
	 the url located in the href is not valid. the url is
	 http://localhost:4567/network/id.
	 Thus we set up the general url for all the requests,
	 and for each new request we add the missing part, like
	 network, /storage , /compute etc.
	 The general url is https://cloud.c3lab.tk.jku.at/occi/.
	 For network list the url is
	 https://cloud.c3lab.tk.jku.at/occi/network
	 the id of the network can be taken from the href though.
	 the id can be found at the end of the local url
	 http://localhost:4567/network/id 
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (collectionType == CollectionType.NETWORK_COLLECTIONS) {
			if (localName.equals(network)) {
				isBaseElement = true;
				BaseElementPO baseElementPO = new BaseElementPO();
				String idString = parseId(attributes, href);
				baseElementPO.setId(idString);
				baseElementPO.setName(attributes.getValue(name));
				baseElementPO.setObjectType(ObjectType.NETWORK);
				baseElementCollection.add(baseElementPO);
			}
		} else if (collectionType == CollectionType.STORAGE_COLLECTIONS) {
			if (localName.equals(storage)) {
				isBaseElement = true;
				BaseElementPO baseElementPO = new BaseElementPO();
				String idString = parseId(attributes, href);
				baseElementPO.setId(idString);
				baseElementPO.setName(attributes.getValue(name));
				baseElementPO.setObjectType(ObjectType.STORAGE);
				baseElementCollection.add(baseElementPO);
			}
		}
		else if(collectionType == CollectionType.COMPUTE_COLLECTIONS)
		{
			if (localName.equals(compute)) {
				isBaseElement = true;
				BaseElementPO baseElementPO = new BaseElementPO();
				String idString = parseId(attributes, href);	
				baseElementPO.setId(idString);
				baseElementPO.setName(attributes.getValue(name));
				baseElementPO.setObjectType(ObjectType.COMPUTE);
				baseElementCollection.add(baseElementPO);
			}
		}
		else if(collectionType==CollectionType.USER_COLLECTIONS)
		{
			if (localName.equals(user)) {
				isBaseElement = true;
				BaseElementPO baseElementPO = new BaseElementPO();
				String idString = parseId(attributes, href);	
				baseElementPO.setId(idString);
				baseElementPO.setName(attributes.getValue(name));
				baseElementPO.setObjectType(ObjectType.USER);
				baseElementCollection.add(baseElementPO);
			}
		}
	}
	private String parseId(Attributes attributes, String valueToParse)
	{
		String idString = "";
		char[] cs = attributes.getValue(valueToParse).toCharArray();
		int length = cs.length;
		int nrOfDigits = 0;
		String  ch1 = "/";
		int k = length-1;
		while(cs[k]!=ch1.charAt(0))
		{
			nrOfDigits++;
			k--;
		}
		Log.d(TAG, "nr of digits : " + nrOfDigits);	
		for(int i = nrOfDigits; i> 0; i--)
		{
			idString +=cs[cs.length - i];
		}
		return idString;
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (isBaseElement) {
			isBaseElement = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String chars = new String(ch, start, length);
		chars = chars.trim();
		super.characters(ch, start, length);
	}

	public List<BaseElementPO> getData() {
		return baseElementCollection;
	}
}
