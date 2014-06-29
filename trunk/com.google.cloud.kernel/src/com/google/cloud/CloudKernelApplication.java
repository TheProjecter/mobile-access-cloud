package com.google.cloud;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.parser.IParserManager;
import com.google.cloud.client.parser.ParserManager;
import com.google.cloud.client.translator.ContentTranslator;
import com.google.cloud.client.translator.IContentTranslator;
import com.google.cloud.db.IDatabaseAdapter;
import com.google.cloud.db.SqlDatabaseAdapter;
import com.google.cloud.provider.DataCache;
import com.google.cloud.provider.DataFactory;
import com.google.cloud.provider.DataProvider;
import com.google.cloud.provider.IDataFactory;
import com.google.cloud.provider.IDataProvider;
import com.google.cloud.publisher.ChangePublisher;
import com.google.cloud.publisher.IChangePublisher;
import com.google.cloud.settings.ISettings;
import com.google.cloud.settings.Settings;
import com.google.cloud.synchronization.ISynchronizationManager;
import com.google.cloud.synchronization.SynchronizationManager;
import com.google.cloud.webservice.IWebServiceClient;
import com.google.cloud.webservice.WebServiceClient;

public class CloudKernelApplication extends Application {
	private static final String TAG = CloudKernelApplication.class
			.getSimpleName();
	public static CloudKernelApplication instance = null;
	private HandlerThread handlerThread = null;
	private Handler handler;
	private boolean isInitialized = false;
	private Object initLock = new Object();

	@Override
	public void onCreate() {
		super.onCreate();
		handlerThread = new HandlerThread("SyncThread");
		handlerThread.start();
		handler = new Handler(handlerThread.getLooper());
		initData();
	}

	public void runaAsync(Runnable runnable) {
		if (runnable == null && handlerThread == null) {
			Log.d(TAG, "runnable null or handlerThread null");
			return;
		}
		handler.post(runnable);
	}

	public void initData() {
		synchronized (initLock) {
			if (!isInitialized) {
				getSettings().loadInitialSettings();
				getSyncManager().startThread();
				initDataCollections();
				this.isInitialized = true;
			}
		}
	}
	
	private void initDataCollections() {
		getSyncManager().updateBaseElementCollections(CollectionType.NETWORK_COLLECTIONS);
		getSyncManager().updateBaseElementCollections(CollectionType.STORAGE_COLLECTIONS);
		getSyncManager().updateBaseElementCollections(CollectionType.COMPUTE_COLLECTIONS);
		getSyncManager().updateBaseElementCollections(CollectionType.USER_COLLECTIONS);
		// getSyncManager().updateStorages();
	}

	public void deInitData() {
		synchronized (initLock) {
			if (isInitialized) {
				getSettings().unLoadAllSettings();
				getSyncManager().stopThread();
				this.isInitialized = false;
			}
		}
	}

	public CloudKernelApplication() {
		super();
		CloudKernelApplication.instance = this;
	}

	public static CloudKernelApplication getInstance() {
		return instance;
	}

	public IWebServiceClient getRestClientInstance() {
		return WebServiceClient.getInstance();
	}

	public ISettings getSettings() {
		return Settings.getInstance();
	}

	public static String getStringResource(int id, String defaultValue) {
		String resource = null;
		try {
			resource = getInstance().getResources().getString(id);
		} catch (Exception ex) {
			Log.e(TAG, "resource with id: " + id + " not found ");
		}
		return resource != null ? resource : defaultValue;
	}
	public IChangePublisher getChangePublisher()
	{
		return ChangePublisher.getInstance();
	}
	public IParserManager getParserManager() {
		return ParserManager.getInstance();
	}

	public IContentTranslator getContentTranslator() {
		return ContentTranslator.getInstance();
	}

	public IDataProvider getDataProvider() {
		return DataProvider.getInstance();
	}

	public IWebServiceClient getWebServiceClient() {
		return WebServiceClient.getInstance();
	}

	public IDataFactory getDataFactory() {
		return DataFactory.getInstance();
	}

	public IDatabaseAdapter getDatabaseAdapter() {
		return SqlDatabaseAdapter.getInstance();
	}

	public DataCache getDataCache() {
		return DataCache.getInstance();
	}

	public ISynchronizationManager getSyncManager() {
		return SynchronizationManager.getInstance();
	}
}
