package com.google.cloud.settings;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public interface ISettings {

	/**
	 *deletes a setting from the list
	 * @param setting the setting to be deleted
	 */
	public void removeSetting(ISetting<?> setting);
	/**
	 * add a setitng to the settings list
	 * @param setting the setting to be added
	 */
	public void addSetting(ISetting<?> setting);
	/**
	 * adds a list of settings to the current list of settings
	 * @param settings the list to be added
	 */
	public void addSettings(List<ISetting<?>> settings);
	/**
	 * gets a string setting based on a key. if the key doesn't exist the default value wil be returned
	 * @return a new setting.
	 */
	public String getSetting(String key , String defaultValue) throws ClassCastException;
	/**
	 * loads the initial settings needed for communicating with the cloud service
	 */
	public void loadInitialSettings();
	/**
	 * gets the cloud protocol. https or http. by default https is used.
	 * @return
	 */
	public String getCloudProtocol();
	/**
	 * gets the cloud server name. it can be an ip or an address.
	 * @return the server name.
	 */
	public String getCloudServerName();
	/**
	 * gets the cloud application context.the personlized cloud service name. in our case is sunstone.
	 * @return the cloud application context.
	 */
	public String getCloudApplicationContext();
	/**
	 * gets the cloud service url;
	 * @return the cloud service url.
	 */
	public URL getServerUrl() throws MalformedURLException;
	/**
	 * unload all settings;
	 */
	public void unLoadAllSettings();
}
