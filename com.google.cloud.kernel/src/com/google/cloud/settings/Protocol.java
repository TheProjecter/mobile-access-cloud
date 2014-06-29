package com.google.cloud.settings;


public enum Protocol {

	HTTP("http"),
	HTTPS("https");

	private String protocolStr;

	private Protocol(String protocolStr) {

		this.protocolStr = protocolStr;
	}

	public static Protocol fromString(String protocolStr) {

		return Protocol.valueOf(protocolStr.toUpperCase());
	}

	@Override
	public String toString() {

		return this.protocolStr;
	}
}
