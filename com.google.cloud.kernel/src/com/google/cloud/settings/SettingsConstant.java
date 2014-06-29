package com.google.cloud.settings;

import java.util.ArrayList;



import com.google.cloud.CloudKernelApplication;
import com.google.cloud.R;

public enum SettingsConstant 
{
	CLOUD_PROTOCOL(new Setting<String>("Protocol",
								  SettingType.STRING,
								  CloudKernelApplication.getStringResource(R.string.default_protocol, 
										  								   Protocol.HTTPS.toString()), 
								  new ArrayList<String>()
								  {
									{
										add(Protocol.HTTP.toString());
										add(Protocol.HTTPS.toString());
									}
								  }
								  )),
	CLOUD_SERVER_NAME(new Setting<String>("ServerName", SettingType.STRING,
									CloudKernelApplication.getStringResource(R.string.default_server_name, null),
									null)),
	CLOUD_APPLICATION_CONTEXT(new Setting<String>("CloudApplicationContext",SettingType.STRING,
												   CloudKernelApplication.getStringResource(R.string.default_application_context, null),
												   null));
	private Setting<?> setting;
	private SettingsConstant(Setting<?> setting)
	{
		this.setting = setting;
	}
	public Setting<?> getSetting()
	{
		return this.setting;
	}
}
