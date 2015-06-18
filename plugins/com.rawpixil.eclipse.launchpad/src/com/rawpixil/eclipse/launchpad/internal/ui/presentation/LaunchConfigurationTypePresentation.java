package com.rawpixil.eclipse.launchpad.internal.ui.presentation;

import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import com.rawpixil.eclipse.launchpad.internal.ui.IPresentation;

public class LaunchConfigurationTypePresentation implements IPresentation<ILaunchConfigurationType> {

	private IDebugModelPresentation presentation;

	LaunchConfigurationTypePresentation(IDebugModelPresentation presentation) {
		this.presentation = presentation;
	}

	@Override
	public Image getImage(ILaunchConfigurationType model) {
		return this.presentation.getImage(model);
	}

	@Override
	public String getText(ILaunchConfigurationType model) {
		return this.presentation.getText(model);
	}

	@Override
	public StyledString getStyledText(ILaunchConfigurationType model) {
		return new StyledString(this.getText(model));
	}

}
