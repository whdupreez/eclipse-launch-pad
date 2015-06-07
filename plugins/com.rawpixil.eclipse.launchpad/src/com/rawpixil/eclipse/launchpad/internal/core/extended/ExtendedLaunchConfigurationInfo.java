package com.rawpixil.eclipse.launchpad.internal.core.extended;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rawpixil.eclipse.launchpad.internal.util.Assert;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public class ExtendedLaunchConfigurationInfo {

	private Map<String, Object> attributes;

	public ExtendedLaunchConfigurationInfo(Map<String, Object> attributes) {
		Assert.notNull(attributes, "Attributes cannot be null.");
		this.attributes = Collections.synchronizedMap(new HashMap<String, Object>(attributes));
	}

	public void setAttributes(Map<String, Object> attributes) {
		Assert.notNull(attributes, "Attributes cannot be null.");
		synchronized (this.attributes) {
			this.attributes.clear();
			this.attributes.putAll(attributes);
		}
	}

	public Map<String, Object> getAttributes() {
		return Collections.unmodifiableMap(this.attributes);
	}

	public <T> Optional<T> getAttribute(String name, Class<T> type) {
		Assert.notNull(name, "Attribute name cannot be null");
		Assert.notNull(type, "Attribute type cannot be null");
		Object value = this.attributes.get(name);
		if (value == null) {
			return Optional.empty();
		}
		else {
			Assert.state(type.isAssignableFrom(value.getClass()), "Attribute value is of an unexpected type.");
			return Optional.of(type.cast(value));
		}
	}

	public void setAttribute(String name, Object value) {
		Assert.notNull(name, "Attribute name cannot be null");
		this.attributes.put(name, value);
	}

}
