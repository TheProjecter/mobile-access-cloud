package com.google.cloud.settings;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.cloud.CloudKernelApplication;

public class Settings implements ISettings {
	private List<ISetting<?>> settingsList = null;
	private static ISettings instance = null;
	private Context context = null;
	private String anonymous_user = "anonymous";

	protected Settings(Context context) {
		this.context = context;
		settingsList = new ArrayList<ISetting<?>>();
	}

	public static ISettings getInstance() {
		if (instance == null) {
			instance = new Settings(CloudKernelApplication.getInstance().getApplicationContext());
		}
		return instance;
	}

	@Override
	public void addSetting(ISetting<?> setting) {
		if (settingsList == null) {
			settingsList = new ArrayList<ISetting<?>>();
		}
		if (setting != null && !settingsList.contains(setting)) {
			settingsList.add(setting);
		}
	}

	@Override
	public void removeSetting(ISetting<?> setting) {
		if (settingsList == null && settingsList.isEmpty()) {
			return;
		} else if (setting != null && !settingsList.contains(setting)) {
			settingsList.remove(setting);
		}
	}

	@Override
	public void addSettings(List<ISetting<?>> settings) {
		if (settingsList == null) {
			settingsList = new ArrayList<ISetting<?>>();
		}
		if (settings != null) {
			for(ISetting<?> setting : settings)	
			{
				if(!settingsList.contains(setting))
				{
					settingsList.add(setting);
				}
			}
		}
	}
	/**
	 * for now this is more than we need. it returns the default shared prefs manager.
	 * if later we decide to store something in the preferences this can be of help.
	 * @return the default value from the prefs manager.
	 */
	private SharedPreferences getSharedPrefs() {
		// SharedPreferences shared =
		// context.getSharedPreferences(anonymous_user,
		// PreferenceActivity.MODE_PRIVATE);
		return PreferenceManager
				.getDefaultSharedPreferences(CloudKernelApplication
						.getInstance().getApplicationContext());
	}
	@Override
	public String getCloudProtocol()
	{
		ISetting<?> protocolSetting = SettingsConstant.CLOUD_PROTOCOL.getSetting();
		return (String)protocolSetting.getValue();
	}
	@Override
	public String getCloudServerName()
	{
		ISetting<?> serverNameSetting = SettingsConstant.CLOUD_SERVER_NAME.getSetting();
		return (String) serverNameSetting.getValue();
	}
	@Override
	public String getCloudApplicationContext()
	{
		ISetting<?> cloudApplicationSetting = SettingsConstant.CLOUD_APPLICATION_CONTEXT.getSetting();
		return(String) cloudApplicationSetting.getValue();
	}
	@Override
	public String getSetting(String name, String defaultValue)
			throws ClassCastException {
		SharedPreferences pref = getSharedPrefs();
		return pref == null ? null : pref.getString(name, defaultValue);
	}
	@Override
	public void loadInitialSettings()
	{
		this.addSetting(SettingsConstant.CLOUD_PROTOCOL.getSetting());
		this.addSetting(SettingsConstant.CLOUD_SERVER_NAME.getSetting());
		this.addSetting(SettingsConstant.CLOUD_APPLICATION_CONTEXT.getSetting());
	}
	@Override
	public URL getServerUrl() throws MalformedURLException
	{
		return getInternalUrl(getCloudProtocol(), 
							  getCloudServerName(), 
							  getCloudApplicationContext());
	}
	private URL getInternalUrl(String protocol, String server_name, String cloud_app_context) throws MalformedURLException
	{
		String urlToReturn = "";
		if(protocol==null || protocol.equals(""))
		{
			protocol = Protocol.HTTPS.toString();
		}
		urlToReturn +=protocol;
		urlToReturn +="://";
		if(server_name!=null && !server_name.equals(""))
		{
			urlToReturn +=server_name;
		}
		else
		{
			return null;
		}
		urlToReturn +="/";
		if(cloud_app_context!=null && !cloud_app_context.equals(""))
		{
			urlToReturn +=cloud_app_context;
		}
		else
		{
			return null;
		}
		return new URL(urlToReturn);
	}

	@Override
	public void unLoadAllSettings() {
		if(settingsList!=null && !settingsList.isEmpty())
		{
			settingsList.clear();
		}
	}
}
