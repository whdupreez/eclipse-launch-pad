package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.util.Images;

public class ExtendedLaunchConfigurationLabelProvider extends LabelProvider {

	private IDebugModelPresentation presentation;

	public ExtendedLaunchConfigurationLabelProvider() {
		this.presentation = DebugUITools.newDebugModelPresentation();
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof IExtendedLaunchConfiguration) {
			IExtendedLaunchConfiguration extended = (IExtendedLaunchConfiguration) element;
			ILaunchConfiguration config = extended.getLaunchConfiguration();
			if (!config.exists()) {
				return Images.getImage(Images.ICON_INVALID_CONFIGURATION);
			}
			else {
				Image image = presentation.getImage(config);
				if (extended.isFavorite()) {
					image = overlayBookmarkIcon(config.getName(), image, Images.ICON_FAVORITES_OVERLAY);
				}
				return image;
			}
		}
		return Images.getMissingImage();
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IExtendedLaunchConfiguration) {
			ILaunchConfiguration config = ((IExtendedLaunchConfiguration) element).getLaunchConfiguration();
			return presentation.getText(config);
		}
		return "Unknown Item";
	}

	private Image overlayBookmarkIcon(String baseName, Image base, String overlayName) {
		return Images.getImageOverlay(baseName, base, overlayName, IDecoration.TOP_LEFT);
	}

}
