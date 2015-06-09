package com.rawpixil.eclipse.launchpad.internal.util;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.rawpixil.eclipse.launchpad.LaunchPadPlugin;

/**
 * Image and icon utilities.
 *
 * @author Willy du Preez
 *
 */
public final class Images {

	public static final String ICON_FAVORITES_OVERLAY = "favorites_overlay.gif";
	public static final String ICON_FAVORITES = "favorites.gif";

	public static final String ICON_INVALID_CONFIGURATION = "invalid_build_tool.gif";

	public static final String ICON_TERMINATE_AND_RELAUNCH = "term_restart.gif";

	private static final String ICON_PATH = "icons/";

	private static final String MISSING_IMAGE = "_missing";

	/**
	 * Returns an image descriptor for the icon file at the given plug-in relative
	 * path. The image descriptor is cached.
	 *
	 * @param name the icon file path
	 * @return the image descriptor for the icon
	 */
	public static final ImageDescriptor getImageDescriptor(String name) {
		ImageRegistry registry = LaunchPadPlugin.getDefault().getImageRegistry();
		ImageDescriptor descriptor = registry.getDescriptor(name);
		if (descriptor == null) {
			Optional<ImageDescriptor> shared = Images.getSharedImageDesciptor(name);
			if (shared.isPresent()) {
				descriptor = shared.get();
			}
			else {
				descriptor = LaunchPadPlugin.imageDescriptorFromPlugin(
						LaunchPadPlugin.PLUGIN_ID, ICON_PATH + name);
			}

		}
		return descriptor;
	}

	private static final Optional<ImageDescriptor> getSharedImageDesciptor(String name) {
		IWorkbench workbench = PlatformUI.isWorkbenchRunning() ? PlatformUI.getWorkbench() : null;
		ImageDescriptor imageDescriptor = workbench == null ?
				null : workbench.getSharedImages().getImageDescriptor(name);
		return Optional.ofNullable(imageDescriptor);
	}

	public static final Image getMissingImage() {
		ImageRegistry registry = LaunchPadPlugin.getDefault().getImageRegistry();
		Image image = registry.get(MISSING_IMAGE);
		if (image == null) {
			ImageDescriptor descriptor = ImageDescriptor.getMissingImageDescriptor();
			registry.put(MISSING_IMAGE, descriptor);
			image = registry.get(MISSING_IMAGE);
		}
		return image;
	}

	public static final Image getImage(String name) {
		ImageRegistry registry = LaunchPadPlugin.getDefault().getImageRegistry();
		Image image = registry.get(name);
		if (image == null) {
			ImageDescriptor descriptor = getImageDescriptor(name);
			registry.put(name, descriptor);
			image = registry.get(name);
		}
		return image;
	}

	public static Image getImageOverlay(String baseName, Image base, String overlayName, int quadrant) {
		ImageRegistry registry = LaunchPadPlugin.getDefault().getImageRegistry();
		String key = baseName + "-" + overlayName + "-" + quadrant;
		Image image = registry.get(key);
		if (image == null) {
			ImageDescriptor overlay = getImageDescriptor(overlayName);
			ImageDescriptor descriptor = new DecorationOverlayIcon(base, overlay, quadrant);
			registry.put(key, descriptor);
			image = registry.get(key);
		}
		return image;
	}

	private Images() {
	}

}
