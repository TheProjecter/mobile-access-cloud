package com.google.cloud.client.properties;

import com.google.cloud.client.objects.BaseElement;

public abstract class Property<T extends Object> extends BaseProperty {
	private boolean isChangeAllowed = false;
	private boolean isVisible = true;
	private BaseElement owner;
	private T value;

	public Property(String key, T value) {
		super(key);
		this.value = value;
	}
	public BaseElement getOwner()
	{
		return this.owner;
	}
	public void setOwner(BaseElement element)
	{
		this.owner = element;
	}
	public Property(String key, T value, Boolean isChangeAllowed)
	{
		this(key, value);
		this.isChangeAllowed = isChangeAllowed;
	}
	public T getValue() {
		return this.value;
	}
	public void setValue(T value)
	{
		this.value = value;
	}
	public boolean isChangeAllowed() {
		return this.isChangeAllowed;
	}

	public void setChangeAllowed(boolean isChangeAllowed) {
		this.isChangeAllowed = isChangeAllowed;
	}

	protected String getStringOrEmpty(String value) {
		if (value == null) {
			value = "";
		}
		return value;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Property) {
			return internalEquals((Property<?>) other);
		}
		return super.equals(other);
	}

	private boolean internalEquals(Property<?> other) {
		if (other != null) {
			if (!super.equals(other)) {
				return false;
			}
			if (this.getValue() != null) {
				if (other.getValue() == null) {
					return false;
				} else if (!this.getValue().equals(other.getValue())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	public boolean getIsVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
