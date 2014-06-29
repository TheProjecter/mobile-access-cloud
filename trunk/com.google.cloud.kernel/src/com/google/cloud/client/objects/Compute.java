package com.google.cloud.client.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Compute extends BaseElement {
	private List<AttachedStorage> attachedStorageList;
	private List<AttachedNetwork> attachedNetworkList;

	public Compute(String computeId) {
		this(computeId, "");
	}
	public Compute(String computeId, String name, String objectType)
	{
		super(computeId,objectType,name);
		attachedStorageList = new ArrayList<AttachedStorage>();
		attachedNetworkList = new ArrayList<AttachedNetwork>();
	}
	public Compute(String computeId, String name) {
		this(computeId, name, ObjectType.COMPUTE.getType());
	}

	public List<AttachedStorage> getAttachedStorages() {
		return this.attachedStorageList;
	}

	public void addStorage(AttachedStorage s) {
		if (attachedStorageList != null && !attachedStorageList.contains(s)) {
			this.attachedStorageList.add(s);
			s.setOwner(this);
		}
	}

	public List<AttachedNetwork> getAttachedNetworks() {
		return this.attachedNetworkList;
	}

	public void addNetwork(AttachedNetwork n) {
		if (attachedNetworkList != null && !attachedNetworkList.contains(n)) {
			this.attachedNetworkList.add(n);
			n.setOwner(this);
		}
	}

	private InstanceType instanceType;

	public InstanceType getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(InstanceType instanceType) {
		this.instanceType = instanceType;
	}

	private HashMap<String, String> context = null;

	/**
	 * This is currently not loaded from OCCI but used for VM creation
	 * 
	 * @return
	 */
	public HashMap<String, String> getContext() {
		if (context == null) {
			context = new HashMap<String, String>();
		}
		return context;
	}
}