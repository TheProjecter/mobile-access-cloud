package com.google.cloud.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.cloud.client.objects.AttachedNetwork;
import com.google.cloud.client.objects.AttachedStorage;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.client.properties.Property;
import com.google.cloud.client.properties.StringProperty;
import com.google.cloud.db.objects.BaseElementRow;
import com.google.cloud.db.objects.BasePropertyRow;
import com.google.cloud.db.objects.ComputeRow;
import com.google.cloud.db.objects.DiskRow;
import com.google.cloud.db.objects.NicRow;
/**
 * class for the purpose of composing objects to display on screen or for
 * composing database objects for the purpose of storing.
 * @author Andrei
 *
 */
public class DatabaseAdapterUtils {

	public static BaseElement convertElementRowToElement(BaseElementRow row,
			Map<String, List<BasePropertyRow>> propRows) {
		BaseElement baseElement = null;
		baseElement = new BaseElement(row.getBaseElementId(), row.getObjectType());
		List<StringProperty> stringPropList = computeProperties(propRows);
		baseElement.addPropertyList(stringPropList);
		return baseElement;
	}
	public static Compute convertComputeRowToCompute(ComputeRow computeRow, 
												     Map<String,List<BasePropertyRow>> propRows,
												     Map<String, List<NicRow>> nicRows,
												     Map<String, List<DiskRow>> diskRows)
	{
		Compute compute = null;
		compute = new Compute(computeRow.getId(), 
							  computeRow.getObjectType(), 
							  computeRow.getName());
		List<StringProperty> stringPropList = computeProperties(propRows);
		compute.addPropertyList(stringPropList);
		List<AttachedStorage> attachedStorages = computeStorages(diskRows);
		List<AttachedNetwork> attachedNetworks = computeNetworks(nicRows);
		for(AttachedNetwork network : attachedNetworks)
		{
			compute.addNetwork(network);
		}
		for(AttachedStorage storage : attachedStorages)
		{
			compute.addStorage(storage);
		}
		return compute;
	}
	private static List<AttachedStorage> computeStorages(Map<String, List<DiskRow>> diskRowsNap)
	{
		List<AttachedStorage> storageList = new ArrayList<AttachedStorage>();
		if(diskRowsNap==null)
		{
			return null;
		}
		Collection<List<DiskRow>> diskCollection = diskRowsNap.values();
		Iterator<List<DiskRow>> diskIterator = diskCollection.iterator();
		// List<Property<Object>> propList = new ArrayList<Property<Object>>();
		while (diskIterator.hasNext()) {
			List<DiskRow> diskList = diskIterator.next();
			for (DiskRow diskRow : diskList) {
				AttachedStorage storage = new AttachedStorage();
				storage.setId(diskRow.getUid());
				storage.setStorageId(diskRow.getBaseElementId());
				storage.setType(diskRow.getType());
				storage.setTarget(diskRow.getTarget());
				storageList.add(storage);
				//baseElement.addProperty(prop);
			}
		}
		return storageList;
	}
	private static List<AttachedNetwork> computeNetworks(Map<String, List<NicRow>> nicRowsMap)
	{
		List<AttachedNetwork> networkList = new ArrayList<AttachedNetwork>();
		if(nicRowsMap==null)
		{
			return null;
		}
		Collection<List<NicRow>> nicCollection = nicRowsMap.values();
		Iterator<List<NicRow>> nicIterator = nicCollection.iterator();
		// List<Property<Object>> propList = new ArrayList<Property<Object>>();
		while (nicIterator.hasNext()) {
			List<NicRow> nicList = nicIterator.next();
			for (NicRow nicRow : nicList) {
				AttachedNetwork network = new AttachedNetwork();
				network.setId(nicRow.getNicUid());
				network.setNetworkId(nicRow.getBaseElementId());
				network.setIp(nicRow.getNicIp());
				network.setMac(nicRow.getNicMac());
				networkList.add(network);
				//baseElement.addProperty(prop);
			}
		}
		return networkList;
	}
	private static List<IDatabaseRow> extractDiskRows(Compute compute) {
		List<IDatabaseRow> rows = new ArrayList<IDatabaseRow>();
		for (AttachedStorage storage : compute.getAttachedStorages()) {
			DiskRow diskRow = new DiskRow(storage.getId(),
										  compute.getElementId(), 
										  storage.getStorageId(),
										  storage.getType(), 
										  storage.getTarget());
			rows.add(diskRow);
		}
		return rows;
	}
	private static List<StringProperty> computeProperties(Map<String, List<BasePropertyRow>> propRows)
	{
		List<StringProperty> stringPropList = new ArrayList<StringProperty>();
		if(propRows==null)
		{
			return null;
		}
		Collection<List<BasePropertyRow>> props = propRows.values();
		Iterator<List<BasePropertyRow>> propIterator = props.iterator();
		// List<Property<Object>> propList = new ArrayList<Property<Object>>();
		while (propIterator.hasNext()) {
			List<BasePropertyRow> propList = propIterator.next();
			for (BasePropertyRow propRow : propList) {
				StringProperty prop = new StringProperty(propRow.getPropertyType(), 
														 propRow.getPropertyName(),
														 propRow.isChangeAllowed());
				prop.setVisible(propRow.getIsVisible());
				stringPropList.add(prop);
				//baseElement.addProperty(prop);
			}
		}
		return stringPropList;
	}
	private static List<IDatabaseRow> getComputeRows(Compute compute) {
		ComputeRow computeRow = new ComputeRow(compute.getElementId(), 
												compute.getObjectType(),
												compute.getName());
		List<IDatabaseRow> rows = new ArrayList<IDatabaseRow>();
		rows.add(computeRow);
		List<IDatabaseRow> propertyRows = extractProperties(compute);
		rows.addAll(propertyRows);
		List<IDatabaseRow> nicRows = extractNicRows(compute);
		List<IDatabaseRow> diskRows = extractDiskRows(compute);
		rows.addAll(nicRows);
		rows.addAll(diskRows);
		return rows;
	}

	private static List<IDatabaseRow> extractNicRows(Compute compute) {
		List<IDatabaseRow> rows = new ArrayList<IDatabaseRow>();
		for (AttachedNetwork network : compute.getAttachedNetworks()) {
			NicRow nicRow = new NicRow(network.getId(), 
									   compute.getElementId(),
									   network.getNetworkId(), 
									   network.getIp(), 
									   network.getMac());
			rows.add(nicRow);
		}
		return rows;
	}

	public static List<IDatabaseRow> getDatabaseRows(BaseElement baseElement) {
		List<IDatabaseRow> rows = new ArrayList<IDatabaseRow>();
		if (baseElement.getObjectType().equals(ObjectType.COMPUTE.getType())) {
			rows = getComputeRows((Compute) baseElement);
		} else if (baseElement.getObjectType().equals(ObjectType.NETWORK.getType()) || 
				   baseElement.getObjectType().equals(ObjectType.STORAGE.getType())) {
			rows = getBaseElementRows(baseElement);
		}
		return rows;
	}

	private static List<IDatabaseRow> getBaseElementRows(BaseElement baseElement) {
		List<IDatabaseRow> rows = new ArrayList<IDatabaseRow>();
		BaseElementRow row = new BaseElementRow(baseElement.getElementId(),
											    baseElement.getName(), 
											    baseElement.getObjectType());
		List<IDatabaseRow> properties = extractProperties(baseElement);
		rows.add(row);
		rows.addAll(properties);
		return rows;
	}

	private static List<IDatabaseRow> extractProperties(BaseElement baseElement) {
		List<IDatabaseRow> rows = new ArrayList<IDatabaseRow>();
		for (Property<?> prop : baseElement.getPropertyList()) {
			BasePropertyRow row = new BasePropertyRow(baseElement.getElementId(), 
													  prop.getKey(),
													  String.valueOf(prop.getValue()), 
													  prop.isChangeAllowed(),
													  prop.getIsVisible());
			rows.add(row);
		}
		return rows;
	}

	/**
	 * for database manipulation we need the generic cols to be strings, thus
	 * because the return format is an array of GenericColumns, they need to be
	 * converted from an array of generic column to an array of strings.
	 * 
	 * @return the converted array of generic columns
	 */
	public static String[] getColumnNames(TablesCreate table) {

		List<String> cols = new ArrayList<String>();
		for (GenericColumn col : table.getColumns()) {
			cols.add(col.getFieldName());
		}
		String[] colNames = new String[cols.size()];
		colNames = cols.toArray(colNames);
		return colNames;
	}
}
