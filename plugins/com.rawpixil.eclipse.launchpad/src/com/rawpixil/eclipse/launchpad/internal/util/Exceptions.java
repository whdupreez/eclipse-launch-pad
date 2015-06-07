package com.rawpixil.eclipse.launchpad.internal.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.rawpixil.eclipse.launchpad.LaunchPadPlugin;

public final class Exceptions {

	public static CoreException coreException(String message) {
		return new CoreException(new Status(IStatus.ERROR, LaunchPadPlugin.PLUGIN_ID, message));
	}

	private Exceptions() {
	}

}
