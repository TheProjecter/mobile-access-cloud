package com.google.cloud.utils;



public class ItemValuePair {

	protected String key;
	protected String value;

	public ItemValuePair(String key, String value) {

		this.key = key;
		this.value = value;
	};

	public void setKey(String key) {

		this.key = key;

	}

	public String getKey() {

		if (this.key == null) {
			return "";
		}
		return this.key;
	}

	public void setValue(String value) {

		this.value = value;
	}

	public String getValue() {

		if (this.value == null) {
			return "";
		}
		return this.value;
	}

	@Override
	public String toString() {

		return value == null ? "" : value.toString();
	}

	@Override
	public boolean equals(Object other) {

		if (other instanceof ItemValuePair) {
			return checkEquals((ItemValuePair) other);
		} else
			return false;
	}

	protected boolean checkEquals(ItemValuePair other) {

		if (other != null) {
			if (this.getKey() == null) {
				if (other.getKey() != null) {
					return false;
				}
			} else {
				if (!this.getKey().equals(other.getKey())) {
					return false;
				}
			}
			if (this.getValue() == null) {
				if (other.getValue() != null) {
					return false;
				}
			} else {
				if (!this.getValue().equals(other.getValue())) {
					return false;
				}
			}
			return true;
		} else
			return false;
	}
}