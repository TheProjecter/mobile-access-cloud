package com.google.cloud.ui;

import com.google.cloud.CloudKernelApplication;

public class CloudApplication extends CloudKernelApplication {
	@Override
	public void onCreate() {
		super.onCreate();
	}

	public CloudApplication() {
		super();
		CloudKernelApplication.instance = this;
	}
}
