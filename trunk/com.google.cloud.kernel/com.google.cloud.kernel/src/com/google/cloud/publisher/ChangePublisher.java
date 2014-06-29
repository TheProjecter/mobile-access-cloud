package com.google.cloud.publisher;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.client.objects.BaseElement;

public class ChangePublisher implements IChangePublisher 
{
	private List<IElementChangeListener> listeners;
	private static IChangePublisher instance = null;
	protected ChangePublisher()
	{
		
	}
	public static IChangePublisher getInstance()
	{
		if(instance==null)
		{
			instance = new ChangePublisher();
		}
		return instance;
	}
	@Override
	public void register(IElementChangeListener listener)
	{
		if(listeners==null)
		{
			listeners = new ArrayList<IElementChangeListener>();
		}
		if(listener!=null && !listeners.contains(listener))
		{
			listeners.add(listener);
		}
	}
	@Override
	public void unregister(IElementChangeListener listener)
	{
		if(listeners.isEmpty())
		{
			return;
		}
		if(listener!=null && listeners.contains(listener))
		{
			listeners.remove(listener);
		}
	}
	@Override
	public void notifyElementChanged(BaseElement element)
	{
		if(listeners==null || listeners.isEmpty() || element==null)
		{
			return;
		}
		for(IElementChangeListener listener : listeners)
		{
			if(listener!=null)
			{
				fireElementChanged(listener, element);
			}
		}
	}
	private void fireElementChanged(IElementChangeListener listener, BaseElement element)
	{
		listener.onElementChanged(element);
	}
}
