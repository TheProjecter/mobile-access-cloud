package com.google.cloud.client.objects;

import java.util.UUID;

/**
 * Represents a network card
 * 
 * @author matthias
 */
public class AttachedNetwork {

	private String ip, mac;
	private String networkHref;
	private String networkName;
	private BaseElement baseElement;
	private String networkId;
	private String id;
	public AttachedNetwork() {
		this.id = String.valueOf(UUID.randomUUID());
	}

	public String getIp() {
		return ip;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}
	public String getNetworkId()
	{
		return this.networkId;
	}
	public void setNetworkId(String id)
	{
		this.networkId = id;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getId()
	{
		return this.id;
	}
	public String getNetworkHref()
	{
		return this.networkHref;
	}
	public void setNetworkHref(String networkHref)
	{
		this.networkHref = networkHref;
	}
	public String getNetworkName()
	{
		return this.networkName;
	}
	public void setNetworkName(String name)
	{
		this.networkName = name;
	}

	public void setOwner(BaseElement baseElement) {
		this.baseElement = baseElement;
	}
	public BaseElement getOwner()
	{
		return this.baseElement;
	}
}
