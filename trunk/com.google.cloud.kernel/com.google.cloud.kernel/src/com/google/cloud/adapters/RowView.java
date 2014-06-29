package com.google.cloud.adapters;


public class RowView<T> {
	private T object;
	public RowView(T object)
	{
		this.object = object;
	}
	public T getObject()
	{
		return this.object;
	}
}
