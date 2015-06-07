package com.rawpixil.eclipse.launchpad.internal.core.extended;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchMode;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationWorkingCopy;
import com.rawpixil.eclipse.launchpad.internal.util.Assert;
import com.rawpixil.eclipse.launchpad.internal.util.ELF;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;
import com.rawpixil.eclipse.launchpad.internal.util.comparator.Comparators;

/**
 *
 * @author Willy du Preez
 *
 */
public class ExtendedLaunchConfiguration implements IExtendedLaunchConfiguration {

	private static final String FAVORITE_ATTR = "favourite";
	private static final String DEFAULT_LAUNCH_MODE_ATTR = "default_launch_mode";

	private ExtendedLaunchConfigurationInfo info;
	private ILaunchConfiguration config;

	public ExtendedLaunchConfiguration(ILaunchConfiguration config) {
		Assert.notNull(config, "Launch configuration cannot be null");
		this.config = config;
		this.info = new ExtendedLaunchConfigurationInfo(new HashMap<String, Object>());
	}

	@Override
	public ILaunchConfiguration getLaunchConfiguration() {
		return this.config;
	}

	@Override
	public boolean exists() {
		return this.config.exists();
	}

	@Override
	public IExtendedLaunchConfigurationWorkingCopy getWorkingCopy() {
		return new ExtendedLaunchConfigurationWorkingCopy(
				new ExtendedLaunchConfigurationInfo(this.info.getAttributes()));
	}

	@Override
	public boolean isFavorite() {
		return this.isFavorite(this.info);
	}

	private boolean isFavorite(ExtendedLaunchConfigurationInfo info) {
		Optional<Boolean> favorite = info.getAttribute(FAVORITE_ATTR, Boolean.class);
		if (favorite.isPresent()) {
			return favorite.get().booleanValue();
		}
		return false;
	}

	@Override
	public Optional<String> getDefaultLaunchMode() {
		return this.getDefaultLaunchMode(this.info, this.config);
	}

	private Optional<String> getDefaultLaunchMode(ExtendedLaunchConfigurationInfo info, ILaunchConfiguration config) {
		Optional<String> mode = info.getAttribute(DEFAULT_LAUNCH_MODE_ATTR, String.class);
		if (!mode.isPresent()) {
			List<ILaunchMode> modes = ELF.getLaunchModes();
			Collections.sort(modes, Comparators.launchModeComparator());
			return modes.isEmpty() ? Optional.<String>empty() : Optional.of(modes.get(0).getIdentifier());
		}
		return mode;
	}

	@Override
	public int hashCode() {
		return this.config.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO improve
		if (obj instanceof ExtendedLaunchConfiguration) {
			return this.config.equals(((ExtendedLaunchConfiguration) obj).config);
		}
		return false;
	}

	public class ExtendedLaunchConfigurationWorkingCopy implements IExtendedLaunchConfigurationWorkingCopy {

		private ILaunchConfiguration config;
		private ExtendedLaunchConfigurationInfo info;

		private ExtendedLaunchConfigurationWorkingCopy(ExtendedLaunchConfigurationInfo info) {
			this.info = info;
			this.config = ExtendedLaunchConfiguration.this.config;
		}

		@Override
		public ILaunchConfiguration getLaunchConfiguration() {
			return this.config;
		}

		@Override
		public void setLaunchConfiguration(ILaunchConfiguration config) {
			Assert.notNull(config, "Launch configuration cannot be null.");
			this.config = config;
		}

		@Override
		public boolean exists() {
			return ExtendedLaunchConfiguration.this.exists();
		}

		@Override
		public boolean isFavorite() {
			return ExtendedLaunchConfiguration.this.isFavorite(this.info);
		}

		@Override
		public void setFavorite(boolean favorite) {
			this.info.setAttribute(FAVORITE_ATTR, Boolean.valueOf(favorite));
		}

		@Override
		public Optional<String> getDefaultLaunchMode() {
			return ExtendedLaunchConfiguration.this.getDefaultLaunchMode(this.info, this.config);
		}

		@Override
		public void setDefaultLaunchMode(Optional<String> mode) {
			// TODO What if mode not present?
			this.info.setAttribute(DEFAULT_LAUNCH_MODE_ATTR, mode.get());
		}

		@Override
		public boolean isDirty() {
			return !this.config.equals(ExtendedLaunchConfiguration.this.config)
					|| this.isFavorite() != ExtendedLaunchConfiguration.this.isFavorite()
					|| !this.getDefaultLaunchMode().get().equals(ExtendedLaunchConfiguration.this.getDefaultLaunchMode().get());
		}

		Optional<IExtendedLaunchConfiguration> save() {
			synchronized (ExtendedLaunchConfiguration.this) {
				if (this.isDirty()) {
					ExtendedLaunchConfiguration.this.info.setAttributes(this.info.getAttributes());
					ExtendedLaunchConfiguration.this.config = this.config;
					return Optional.of((IExtendedLaunchConfiguration) ExtendedLaunchConfiguration.this);
				}
				else {
					return Optional.empty();
				}
			}
		}

		@Override
		public IExtendedLaunchConfigurationWorkingCopy getWorkingCopy() {
			throw new UnsupportedOperationException("Nested working copies are not supported.");
		}

	}

}
