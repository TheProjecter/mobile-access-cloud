package com.google.cloud.settings;

import java.util.List;

public interface ISetting<T> {

	/**
	 * returns the generic value which can be either a defaul one
	 * or one stored in the preferences. for now we don't
	 * use the preference manager
	 * @return the new value from the preferences or the default
	 * value.
	 * @throws ClassCastException
	 */
	public T getValue() throws ClassCastException;
	/**
	 * gets a list of possible values.can be null.
	 * @return the list of possible values.
	 */
	public List<T> getPossibleValues();
	/**
	 * gets the type of the setting.
	 * it is needed for parsing the value.
	 * @return the type of the setting.
	 */
	public SettingType getType();
	/**
	 * gets the key of the setting.
	 * every setting has a key and a default value.
	 * @return the key of the setting.
	 */
	public String getKey();
	/**
	 * sets the default value of the setting.
	 * @param value the default value of the setting.
	 */
	public void setDefaultValue(T value);
	/**
	 * gets the default value of the setting.
	 * @return the default value.
	 */
	public T getDefaultValue();

}
