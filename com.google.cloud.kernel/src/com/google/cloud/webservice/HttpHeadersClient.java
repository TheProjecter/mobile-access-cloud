package com.google.cloud.webservice;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import android.util.Log;

/**
 * the purpose of this class is to deal with http requests. not all the http
 * variables will be used but the idea is that you can add the functionality
 * later.
 * 
 * @author Andrei
 * 
 */
public abstract class HttpHeadersClient {
	private static final String TAG = HttpHeadersClient.class.getSimpleName();
	private static final int HTTP_STATUS_OK = 200;
	private static final int HTTP_STATUS_CREATED = 201;
	private static final int HTTP_STATUS_ACCEPTED = 202;
	private static final int HTTP_STATUS_NO_CONTENT = 204;
	private static final int HTTP_STATUS_BAD_REQUEST = 400;
	private static final int HTTP_STATUS_NOT_FOUND = 404;
	private static final int HTTP_STATUS_INTERNAL_ERROR = 500;
	private static final int HTTP_STATUS_NOT_IMPLEMENTED = 501;
	private static final int HTTP_STATUS_UNAUTHORIZED = 401;
	private static final int HTTP_STATUS_FORBIDDEN = 402;
	private int HTTP_STATUS_CODE;

	public HttpHeadersClient() {

	}

	protected HttpEntity executePutRequest(String contentURL, StringEntity body) {
		HttpEntity entity = null;
		HttpPut httpPutRequest = new HttpPut(contentURL);
		httpPutRequest.setEntity(body);
		HttpResponse response = executeHttpPut(httpPutRequest);
		if (response != null) {
			entity = response.getEntity();
		}
		return entity;
	}

	protected HttpEntity executePostRequest(String contentURL, StringEntity body) {
		HttpEntity entity = null;
		HttpPost httpPost = new HttpPost(contentURL);
		
		httpPost.setEntity(body);
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
		HttpResponse response = executeHttpPost(httpPost);
		if (response != null) {
			entity = response.getEntity();
		}
		return entity;
	}

	private synchronized HttpResponse executeHttpPost(HttpUriRequest httpPost) {
		Exception exception = null;
		try {
			HttpClient request = getHttpClient();
			HttpResponse response = request.execute(httpPost);
			handleStatusCode(response);
		} catch (Exception e) {
			Log.e(TAG, "error in executing the post request");
			if (e instanceof HttpResponseException) {
				Log.e(TAG, "HttpResponseException");
				if (this.HTTP_STATUS_CODE == HTTP_STATUS_UNAUTHORIZED) {
					exception = new AuthenticationException(
							"Unauthorized Exception");
				} else {
					exception = new HttpException(
							"Http not authorized Exception");
				}
			} else if (this.HTTP_STATUS_CODE == HTTP_STATUS_BAD_REQUEST) {
				Log.e(TAG, "BAD Request. error in get execute");
				exception = new Exception("Bad Request");
			}
		}
		if (exception != null) {
			Log.e(TAG, "Exception." + exception.getMessage());
		}
		return null;
	}

	private synchronized HttpResponse executeHttpPut(HttpUriRequest httpPut) {
		Exception exception = null;
		try {
			HttpClient request = getHttpClient();
			HttpResponse response = request.execute(httpPut);
			handleStatusCode(response);
			return response;
		} catch (Exception e) {
			Log.e(TAG, "error in executing the put request");
			if (e instanceof HttpResponseException) {
				Log.e(TAG, "HttpResponseException");
				if (this.HTTP_STATUS_CODE == HTTP_STATUS_UNAUTHORIZED) {
					exception = new AuthenticationException(
							"Unauthorized Exception");
				} else {
					exception = new HttpException(
							"Http not authorized Exception");
				}
			} else if (this.HTTP_STATUS_CODE == HTTP_STATUS_BAD_REQUEST) {
				Log.e(TAG, "BAD Request. error in get execute");
				exception = new Exception("Bad Request");
			}
		}
		if (exception != null) {
			Log.e(TAG, "Exception." + exception.getMessage());
		}
		return null;
	}

	protected HttpEntity executeHttpGetRequest(String contentURL)
			throws Exception {
		HttpEntity entity = null;
		HttpGet httpGet = new HttpGet(contentURL);
		HttpResponse response = executeHttpGet(httpGet);
		if (response != null) {
			entity = response.getEntity();
		}
		return entity;
	}

	private synchronized HttpResponse executeHttpGet(HttpUriRequest httpGet) {
		Exception exception = null;
		try {
			HttpClient request = getHttpClient();
			HttpResponse response = request.execute(httpGet);
			handleStatusCode(response);
			return response;
		} catch (Exception e) {
			Log.e(TAG, "error in executing the get request");
			if (e instanceof HttpResponseException) {
				Log.e(TAG, "HttpResponseException");
				if (this.HTTP_STATUS_CODE == HTTP_STATUS_UNAUTHORIZED) {
					exception = new AuthenticationException(
							"Unauthorized Exception");
				} else {
					exception = new HttpException(
							"Http not authorized Exception");
				}
			} else if (this.HTTP_STATUS_CODE == HTTP_STATUS_BAD_REQUEST) {
				Log.e(TAG, "BAD Request. error in get execute");
				exception = new Exception("Bad Request");
			}
		}
		if (exception != null) {
			Log.e(TAG, "Exception." + exception.getMessage());
		}
		return null;
	}

	private void handleStatusCode(HttpResponse response)
			throws ClientProtocolException {
		StatusLine statusLine = response.getStatusLine();
		this.HTTP_STATUS_CODE = statusLine.getStatusCode();
	}

	protected abstract HttpClient getHttpClient() throws Exception;
}
