package com.rawpixil.eclipse.launchpad.internal.ui.presentation;

import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunch;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.core.extended.LaunchPadProvider;
import com.rawpixil.eclipse.launchpad.internal.ui.IPresentation;
import com.rawpixil.eclipse.launchpad.internal.util.Images;

public class ExtendedLaunchConfigurationPresentation implements IPresentation<IExtendedLaunchConfiguration> {

	private IDebugModelPresentation presentation;

	ExtendedLaunchConfigurationPresentation(IDebugModelPresentation presentation) {
		this.presentation = presentation;
	}

	@Override
	public Image getImage(IExtendedLaunchConfiguration model) {
		IExtendedLaunchConfiguration extended = model;
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

	private Image overlayBookmarkIcon(String baseName, Image base, String overlayName) {
		return Images.getImageOverlay(baseName, base, overlayName, IDecoration.TOP_LEFT);
	}

	@Override
	public String getText(IExtendedLaunchConfiguration model) {
		ILaunchConfiguration config = model.getLaunchConfiguration();
		return presentation.getText(config);
	}

	@Override
	public StyledString getStyledText(IExtendedLaunchConfiguration model) {
		List<IExtendedLaunch> launches = LaunchPadProvider.INSTANCE.get().getExtendedLaunches(model);
		if (!launches.isEmpty()) {
			String base = this.getText(model) + "  ";
			String decoration = this.decorate(this.status(launches));
			StyledString styled = new StyledString(base);
			styled.append(decoration, StyledString.DECORATIONS_STYLER);
			return styled;
		}
		else {
			return new StyledString(this.getText(model));
		}
	}

	// TODO Move into Messages.
	private String decorate(final LaunchStatusStruct status) {
		String decoration;
		if (status.active) {
			if (status.processes > 0) {
				decoration = "[Launched, Processes: " + status.processes + "]";
			}
			else {
				decoration = "[Launching ...]";
			}
		}
		else {
			decoration = "[Terminated]";
		}
		return decoration;
	}

	private LaunchStatusStruct status(List<IExtendedLaunch> launches) {
		final LaunchStatusStruct status = new LaunchStatusStruct();
		for (IExtendedLaunch launch : launches) {
			if (!launch.getLaunch().isTerminated()) {
				status.active = true;
				for (IProcess process : launch.getLaunch().getProcesses()) {
					if (!process.isTerminated()) {
						status.processes++;
					}
				}
			}
		}
		return status;
	}

	private class LaunchStatusStruct {
		private boolean active;
		private int processes;
	}

}
