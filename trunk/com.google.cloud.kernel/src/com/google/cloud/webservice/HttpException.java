package com.google.cloud.webservice;
/**
 * class that handles with exceptions of any kind. 
 * not yet used.
 * @author Andrei
 *
 */
public class HttpException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8738539958861473285L;
	private String message=  null;
	private Throwable throwable = null;
	public HttpException(String message)
	{
		this.message = message;
	}
	public HttpException(Throwable throwable)
	{
		super(throwable);
		this.throwable = throwable;
	}
	@Override
	public String getMessage() {
		return this.message;
	}
	public Throwable getCause() 
	{
		return this.throwable;
	}
	@Override
	public String toString() 
	{	
		return this.message;
	}
	
}
