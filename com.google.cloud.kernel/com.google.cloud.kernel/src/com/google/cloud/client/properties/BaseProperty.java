package com.google.cloud.client.properties;


public abstract class BaseProperty{
	private String key;
	public BaseProperty(String key)
	{
		this.key = key;
	}
	public BaseProperty() {
		
	}
	public String getKey()
	{
		return this.key;
	}
	@Override
	public boolean equals(Object other) {
		if (other instanceof BaseProperty)
			return internalEquals((BaseProperty) other);
		else
			return false;
	}

	private boolean internalEquals(BaseProperty other) {
		if (other != null) {

			if (this.getKey() == null) {
				if (other.getKey() != null)
					return false;
			} else {
				if (!this.getKey().equals(other.getKey()))
					return false;
			}

			return true;
		} else {
			return false;
		}
	}

}
