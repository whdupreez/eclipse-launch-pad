package com.rawpixil.eclipse.launchpad.internal.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * {@link Display} utilities.
 *
 * @author Willy du Preez
 *
 */
public final class Displays {

	/**
	 * Returns the display instance
	 *
	 * @return the display instance
	 */
	public static Display getDisplay() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = PlatformUI.getWorkbench().getDisplay();
//			display = Display.getDefault();
		}
		return display;
	}

	/**
	 * Returns the launch pad plug-in shell
	 *
	 * @return the launch pad plug-in shell
	 */
	public static Shell getActiveShell() {
		return Displays.getDisplay().getActiveShell();
	}

	private Displays() {
	}

}
