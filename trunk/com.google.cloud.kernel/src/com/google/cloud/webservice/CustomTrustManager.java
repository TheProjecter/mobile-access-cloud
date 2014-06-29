package com.google.cloud.webservice;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.util.Log;

public class CustomTrustManager implements X509TrustManager {
	private static final String TAG = CustomTrustManager.class.getSimpleName();
	private X509TrustManager defaultTrustManager = null;
	private X509TrustManager localTrustManager = null;
	private X509Certificate[] acceptedCertsArray;
	private List<X509Certificate> acceptedCertsList = new ArrayList<X509Certificate>();

	public CustomTrustManager(KeyStore trustore) {
		this.localTrustManager = new LocalStoreX509TrustManager(trustore);
		initTrustManager((KeyStore) null);
		initCertsList();
	}

	void initTrustManager(KeyStore trustStore) {
		Log.d(TAG, "initializing the keyStore");
		try {
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(trustStore);
			defaultTrustManager = getX509TrustManager(tmf);

		} catch (Exception e) {
			Log.e(TAG,"error in initializing or reading the keystore." + e.getMessage());
			if (e instanceof GeneralSecurityException) {
				Log.e(TAG, "Security Exception");
				throw new RuntimeException();
			}
		}
	}

	private void initCertsList() {
		if (defaultTrustManager == null) {
			throw new IllegalStateException("Couldn't find X509TrustManager");
		}
		for (X509Certificate cert : defaultTrustManager.getAcceptedIssuers()) {
			acceptedCertsList.add(cert);
		}
		acceptedCertsArray = acceptedCertsList.toArray(new X509Certificate[acceptedCertsList.size()]);
	}

	static class LocalStoreX509TrustManager implements X509TrustManager {

		private X509TrustManager localTrustManager;

		LocalStoreX509TrustManager(KeyStore localTrustStore) {
			try {
				TrustManagerFactory tmf = TrustManagerFactory
						.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				tmf.init(localTrustStore);

				localTrustManager = getX509TrustManager(tmf);
				if (localTrustManager == null) {
					throw new IllegalStateException(
							"Couldn't find X509TrustManager");
				}
			} catch (GeneralSecurityException e) {
				throw new RuntimeException(e);
			}

		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			localTrustManager.checkClientTrusted(chain, authType);
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			localTrustManager.checkServerTrusted(chain, authType);
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return localTrustManager.getAcceptedIssuers();
		}
	}

	static X509TrustManager getX509TrustManager(TrustManagerFactory tmf) {
		Log.d(TAG, "getting the x509 trust manager");
		TrustManager[] trustList = tmf.getTrustManagers();
		for (int k = 0; k < trustList.length; k++) {
			if (trustList[k] instanceof X509TrustManager) {
				return (X509TrustManager) trustList[k];
			}
		}
		return null;
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		try {
			Log.d(TAG, "checkServerTrusted() with default trust manager...");
			defaultTrustManager.checkClientTrusted(chain, authType);
		} catch (CertificateException ce) {
			Log.d(TAG, "checkServerTrusted() with local trust manager...");
			localTrustManager.checkClientTrusted(chain, authType);
		}
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		try {
			Log.d(TAG, "checkServerTrusted() with default trust manager...");
			defaultTrustManager.checkServerTrusted(chain, authType);
		} catch (CertificateException ce) {
			Log.d(TAG, "checkServerTrusted() with local trust manager...");
			localTrustManager.checkServerTrusted(chain, authType);
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return acceptedCertsArray;
	}

}
