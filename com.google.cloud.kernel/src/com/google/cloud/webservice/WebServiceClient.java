package com.google.cloud.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.R;
import com.google.cloud.client.objects.BaseElement;
import com.google.cloud.client.objects.CollectionType;
import com.google.cloud.client.objects.Compute;
import com.google.cloud.client.objects.ObjectType;
import com.google.cloud.client.parser.IParserManager;
import com.google.cloud.client.po.BaseElementPO;
import com.google.cloud.client.po.ComputePO;
import com.google.cloud.client.translator.IContentTranslator;
import com.google.cloud.settings.ISettings;
import com.google.cloud.settings.Protocol;

/**
 * 
 * @author Andrei
 * 
 */
public class WebServiceClient extends HttpHeadersClient implements
		IWebServiceClient {

	private static IWebServiceClient instance = null;
	private static final String STOREPASS = "2985sag";
	// private static final String TRUSTSTOREPASS = "";
	protected String endpointUrl = "";
	private static final String GET_NETWORK = "network";
	private static final String GET_STORAGE = "storage";
	private static final String GET_USER = "user";
	private static final String GET_INSTANCE_TYPE = "instance_type";
	private static final String CREATE_COMPUTE = "compute";
	private static final String CREATE_NETWORK = "network";
	private static final int TIMEOUT = 10 * 1000;
	private static final String TAG = WebServiceClient.class.getSimpleName();
	private static final int MAX_CONN_PER_ROUTE = 10;
	private static final String GET_COMPUTE = "compute";

	public WebServiceClient() {
		super();
	}

	public static final IWebServiceClient getInstance() {
		if (instance == null) {
			instance = new WebServiceClient();
		}
		return instance;
	}
	private String getWebURL(CollectionType type)
	{
		String webGETURL = "";
		if(type==CollectionType.NETWORK_COLLECTIONS)
		{
			webGETURL = GET_NETWORK;
		}
		else if(type==CollectionType.STORAGE_COLLECTIONS)
		{
			webGETURL = GET_STORAGE;
		}
		else if(type==CollectionType.COMPUTE_COLLECTIONS)
		{
			webGETURL = GET_COMPUTE;
		}
		else if(type==CollectionType.USER_COLLECTIONS)
		{
			webGETURL = GET_USER;
		}else if(type == CollectionType.INSTANCE_TYPE_COLLECTIONS)
		{
			webGETURL = GET_INSTANCE_TYPE;
		}
		
		return webGETURL;
	}
	/**
	 * gets a network by the client id
	 * 
	 * @param clientUID
	 *            the client id for the network
	 * @return the network instance.
	 */
	private List<BaseElement> getInternalCollections(CollectionType type) {
		List<BaseElement> baseElementList = null;
		String webGETURL = getWebURL(type);
		
		try {
			HttpEntity response = executeHttpGetRequest(getContentStringURL(webGETURL));
			InputStream stream = response.getContent();
			IContentTranslator contentTranslator = CloudKernelApplication
					.getInstance().getContentTranslator();
			String xml = contentTranslator.getXMLFromInputStream(stream);
			IParserManager parser = CloudKernelApplication.getInstance()
					.getParserManager();

			List<BaseElementPO> baseElementCollections = parser.parseBaseElementCollections(xml,type);
			baseElementList = contentTranslator.toBaseElementCollections(baseElementCollections);
		} catch (Exception e) {
			Log.e(TAG, "error in getting the network collection.", e);
		}
		return baseElementList;
	}

	private String getContentStringURL(String... items) {
		String url = "";
		ISettings settings = CloudKernelApplication.getInstance().getSettings();
		try {
			url = settings.getServerUrl().toString();
		} catch (Exception e) {
			Log.e(TAG, "error in getting the URL");
		}
		for (String item : items) {
			url += parseURL(item);
		}
		Log.d(TAG, "url is : " + url.toString());
		return url;
	}

	private String parseURL(String item) {
		String url = "";
		url += "/" + item;
		return url;
	}


	/**
	 * this method creates an instance of the objects that is at the base of the
	 * api secure connections With the help of SSLContext we can create secure
	 * connections.
	 * 
	 * @return new instance of SSLContext.
	 * @throws GeneralSecurityException
	 */
	private SSLContext createSSLContext() throws GeneralSecurityException {
		KeyStore keyStore = getKeyStoreFromInputStream();
		KeyStore trustStore = getTrustStoreFromInputStream();
		CustomTrustManager trustManager = new CustomTrustManager(trustStore);
		TrustManager[] tms = new TrustManager[] { trustManager };
		KeyManagerFactory kmf = getKeyManagerFactory(keyStore);
		kmf.init(keyStore, STOREPASS.toCharArray());
		KeyManager[] kms = kmf.getKeyManagers();
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kms, tms, null);
		return sslContext;
	}

	@Override
	protected synchronized HttpClient getHttpClient() throws Exception {
		SSLContext sslContext = createSSLContext();
		CustomSSLSocketFactory socketFactory = new CustomSSLSocketFactory(
				sslContext,
				SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		HttpClient client = null;
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);// (params,
																	// TIMEOUT);
		ConnPerRoute connPerRoute = new ConnPerRouteBean(MAX_CONN_PER_ROUTE);
		ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
		ConnManagerParams.setMaxTotalConnections(params, 20);
		// registers schemes for both http and https
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme(Protocol.HTTP.toString(),
				PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme(Protocol.HTTPS.toString(),
				socketFactory, 443));
		ClientConnectionManager connManager = new ThreadSafeClientConnManager(
				params, schemeRegistry);
		client = new DefaultHttpClient(connManager, params);
		return client;
	}

	private KeyManagerFactory getKeyManagerFactory(KeyStore keyStore) {
		if (keyStore == null) {
			Log.w(TAG, "keystore is null.please check the keystore");
		}
		KeyManagerFactory kmf = null;
		try {
			kmf = KeyManagerFactory.getInstance(KeyManagerFactory
					.getDefaultAlgorithm());
		} catch (Exception e) {
			Log.e(TAG, "KeyStore problem : " + e.getMessage());
			throw new RuntimeException(e);
		}
		return kmf;
	}

	private KeyStore getTrustStoreFromInputStream() {
		InputStream keyStoreStream = null;
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("PKCS12");
			keyStoreStream = CloudKernelApplication.getInstance()
					.getResources().openRawResource(R.raw.client);
			try {
				keyStore.load(keyStoreStream, STOREPASS.toCharArray());
			} finally {
				keyStoreStream.close();
			}
			return keyStore;
		} catch (Exception e) {
			Log.e(TAG, "keystore problem.check the keystore");
			throw new RuntimeException(e);
		}
	}

	private KeyStore getKeyStoreFromInputStream() {
		InputStream keyStoreStream = null;
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("PKCS12");
			keyStoreStream = CloudKernelApplication.getInstance()
					.getResources().openRawResource(R.raw.client);
			try {
				keyStore.load(keyStoreStream, STOREPASS.toCharArray());
			} finally {
				keyStoreStream.close();
			}
			return keyStore;
		} catch (Exception e) {
			Log.e(TAG, "KeyStore problem : " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public String get(String loc) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete(String loc) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String post(String loc, String input) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void createBaseElement(BaseElement element) {
		Log.d(TAG, "creates a bew object on the server");
		try {
			IContentTranslator translator = CloudKernelApplication
					.getInstance().getContentTranslator();
			String xmlToCreate = translator.getXMLFromObject(element,
					OperationType.CREATE);
			if(xmlToCreate==null)
			{
				Log.w(TAG, "the xml to upload is null. check the object.");
				return;
			}
			StringEntity entityToCreate = new StringEntity(xmlToCreate, "UTF-8");
			
			HttpEntity entity = executePostRequest(
					getContentStringURL(CREATE_NETWORK), entityToCreate);
			InputStream stream = entity.getContent();
			translator.getXMLFromInputStream(stream);
			String result = translator.getXMLFromInputStream(stream);
			Log.d(TAG, "result of the update is : " + result);
		} catch (Exception e) {
			Log.e(TAG, "exception in creating the object.", e);
		}
	}

	@Override
	public BaseElement getBaseElementById(String elementId, ObjectType type) {
		BaseElement baseElement = null;
		try {
			String webGetURL = "";
			if(type==ObjectType.NETWORK)
			{
				webGetURL = GET_NETWORK;
			}
			else if(type==ObjectType.STORAGE)
			{
				webGetURL = GET_STORAGE;
			}
			BaseElementPO baseElementPO = null;
			HttpEntity response = executeHttpGetRequest(getContentStringURL(
					webGetURL, elementId));
			InputStream stream = response.getContent();
			IContentTranslator contentTranslator = CloudKernelApplication
					.getInstance().getContentTranslator();
			String xml = contentTranslator.getXMLFromInputStream(stream);
			IParserManager parser = CloudKernelApplication.getInstance()
					.getParserManager();
			baseElementPO = parser.parseBaseElementPO(xml, type);
			baseElement = contentTranslator.toBaseElement(baseElementPO);
		} catch (Exception e) {
			Log.e(TAG,
					"Exception in getting the network by id." + e.getMessage());
		}
		return baseElement;
	}
	@Override
	public List<BaseElement> getBaseElementCollections(CollectionType type) {
		return getInternalCollections(type);
	}


	@Override
	public void updateBaseElement(BaseElement baseElement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Compute getComputeById(String elementId) {
		Compute compute = null;
		try {
			String webGetURL = GET_COMPUTE;
			ComputePO computePO = null;
			HttpEntity response = executeHttpGetRequest(getContentStringURL(
					webGetURL, elementId));
			InputStream stream = response.getContent();
			IContentTranslator contentTranslator = CloudKernelApplication
					.getInstance().getContentTranslator();
			String xml = contentTranslator.getXMLFromInputStream(stream);
			IParserManager parser = CloudKernelApplication.getInstance()
					.getParserManager();
			computePO = parser.parseComputePO(xml);
			compute = contentTranslator.toCompute(computePO);
		} catch (Exception e) {
			Log.e(TAG,
					"Exception in getting the network by id." + e.getMessage());
		}
		return compute;
	}

	@Override
	public void createComputeElement(Compute computeToUpdate) {
		Log.d(TAG, "creates a new object on the server");
		try {
			IContentTranslator translator = CloudKernelApplication
					.getInstance().getContentTranslator();
			String xmlToCreate = translator.getXMLFromObject(computeToUpdate,
					OperationType.CREATE);
			if(xmlToCreate==null)
			{
				Log.w(TAG, "the xml to upload is null. check the object.");
				return;
			}
			StringEntity entityToCreate = new StringEntity(xmlToCreate, "UTF-8");
			HttpEntity entity = executePostRequest(
					getContentStringURL(CREATE_COMPUTE), entityToCreate);
			InputStream stream = entity.getContent();
			translator.getXMLFromInputStream(stream);
			String result = translator.getXMLFromInputStream(stream);
			Log.d(TAG, "result of the update is : " + result);
		} catch (Exception e) {
			Log.e(TAG, "exception in creating the object.", e);
		}
	}

}
