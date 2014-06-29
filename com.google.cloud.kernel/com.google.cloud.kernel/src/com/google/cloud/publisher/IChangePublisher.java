package com.google.cloud.publisher;

import com.google.cloud.client.objects.BaseElement;

public interface IChangePublisher {

	/**
	 * registers a listener that listens for updates.
	 * @param listener the listener to be reigstered.
	 */
	public void register(IElementChangeListener listener);
	/**
	 * unregister a listener.
	 * @param listener the listener to be unregistered.
	 */
	public void unregister(IElementChangeListener listener);
	/**
	 * notify when an object has changed.
	 * @param element the element that has changed.
	 */
	public void notifyElementChanged(BaseElement element);
}
