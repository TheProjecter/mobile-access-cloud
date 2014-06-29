package com.google.cloud.client.parser;

import com.google.cloud.client.objects.BaseElement;

public interface IDefaultParser {
	/**
	 * returns the data after parsing the xml
	 * @return the parsed xml in to object data.
	 */
	public BaseElement getData();
}
