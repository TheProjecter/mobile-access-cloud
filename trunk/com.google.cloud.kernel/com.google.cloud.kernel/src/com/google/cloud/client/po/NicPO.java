package com.google.cloud.client.po;

public class NicPO {
	private String networkHref;
	private String networkName;
	private String mac;
	private String ip;
	private String networkId;
	public String getNetworkHref() {
		return networkHref;
	}
	public void setNetworkHref(String network) {
		this.networkHref = network;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	public String getNetworkId()
	{
		return this.networkId;
	}
	public void setNetworkId(String networkId)
	{
		this.networkId = networkId;
	}
}
