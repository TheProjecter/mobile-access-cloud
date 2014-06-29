package com.google.cloud.client.parser;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.util.Log;

import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.client.po.BaseElementPO;
import com.google.cloud.client.po.ComputePO;

/**
 * the purpose of this class is to create personalized parsers that 
 * can parse different xmls received from the web client.
 * 
 * @author Andrei
 * 
 */
public class ParserManager implements IParserManager {
	private static IParserManager instance = null;
	private static final String TAG = ParserManager.class.getSimpleName();

	protected ParserManager() {

	}

	public static IParserManager getInstance() {
		if (instance == null) {
			instance = new ParserManager();
		}
		return instance;
	}
	@Override
	public BaseElementPO parseBaseElementPO(String xml, ObjectType type) {
		BaseElementPOParser baseElementPOParser = new BaseElementPOParser(type);
		try {
			baseElementPOParser.parseNetwork(xml);
		} catch (IOException e) {
			Log.e(TAG, "parseToNetworkPO.IOException." + e.getMessage());
		} catch (SAXException e) {
			Log.e(TAG, "parseToNetworkPO.SaxException." + e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(TAG, "parseToNetworkPO.ParserConfigurationException." + e.getMessage());
		}
		return baseElementPOParser.getData();
	}
	@Override
	public ComputePO parseComputePO(String xml) {
		ComputePOParser computeParser = new ComputePOParser();
		try {
			computeParser.parseCompute(xml);
		} catch (IOException e) {
			Log.e(TAG, "parseToComputePO.IOException." + e.getMessage());
		} catch (SAXException e) {
			Log.e(TAG, "parseToComputePO.SaxException." + e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(TAG, "parseToComputePO.ParserConfigurationException." + e.getMessage());
		}
		return computeParser.getData();
	}
	@Override
	public List<BaseElementPO> parseBaseElementCollections(String xml, CollectionType type) {
		BaseElementCollectionsParser baseElementPOParser = new BaseElementCollectionsParser(type);
		try {
			baseElementPOParser.parseBaseElementList(xml);
		} catch (IOException e) {
			Log.e(TAG, "parseToNetworkPO.IOException." + e.getMessage());
		} catch (SAXException e) {
			Log.e(TAG, "parseToNetworkPO.SaxException." + e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.e(TAG, "parseToNetworkPO.ParserConfigurationException." + e.getMessage());
		}
		return baseElementPOParser.getData();
	}
}
