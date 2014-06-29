package com.google.cloud.webservice;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class CustomSSLSocketFactory implements LayeredSocketFactory 
{
	private SSLContext sslContext;
	private X509HostnameVerifier hostNameVerifier;
	private SSLSocketFactory socketFactory;
	public CustomSSLSocketFactory(SSLContext sslContext,
								  X509HostnameVerifier hostNameVerifier)
	{
		this.sslContext = sslContext;
		this.socketFactory = sslContext.getSocketFactory();
		this.hostNameVerifier = hostNameVerifier;
	}
	@Override
	public Socket connectSocket(Socket sock, String host, int port,
			InetAddress localAddress, int localPort, HttpParams params)
			throws IOException, UnknownHostException, ConnectTimeoutException {
		 if (host == null) {
	            throw new IllegalArgumentException("Target host may not be null.");
	        }
	        if (params == null) {
	            throw new IllegalArgumentException("Parameters may not be null.");
	        }

	        SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());

	        if ((localAddress != null) || (localPort > 0)) {
	            if (localPort < 0)
	                localPort = 0;

	            InetSocketAddress isa = new InetSocketAddress(localAddress,
	                    localPort);
	            sslsock.bind(isa);
	        }

	        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
	        int soTimeout = HttpConnectionParams.getSoTimeout(params);

	        InetSocketAddress remoteAddress = new InetSocketAddress(host, port);

	        sslsock.connect(remoteAddress, connTimeout);

	        sslsock.setSoTimeout(soTimeout);
	        try {
	            hostNameVerifier.verify(host, sslsock);
	        } catch (IOException iox) {
	            try {
	                sslsock.close();
	            } catch (Exception x) {
	            }

	            throw iox;
	        }

	        return sslsock;
	}

	@Override
	public Socket createSocket() throws IOException {
		return socketFactory.createSocket();
	}

	@Override
	public boolean isSecure(Socket sock) throws IllegalArgumentException {
		if(sock==null)
		{
			throw new IllegalArgumentException("Socket can't be null");
		}
		if(!(sock instanceof SSLSocket))
		{
			throw new IllegalArgumentException("sock not instance of SSLSocket.Connection must be secure");
		}
		if(sock.isClosed())
		{
			throw new IllegalArgumentException("Sock connection must be open.");
		}
		return true;
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		SSLSocket localSocket = (SSLSocket)socketFactory.createSocket(socket, host, port,autoClose);
		hostNameVerifier.verify(host, localSocket);
		return localSocket;
	}

}
