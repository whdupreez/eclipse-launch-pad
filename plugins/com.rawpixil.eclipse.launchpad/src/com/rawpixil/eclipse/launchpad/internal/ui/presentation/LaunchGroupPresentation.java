package com.rawpixil.eclipse.launchpad.internal.ui.presentation;

import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.ILaunchGroup;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import com.rawpixil.eclipse.launchpad.internal.ui.IPresentation;

public class LaunchGroupPresentation implements IPresentation<ILaunchGroup> {

	private IDebugModelPresentation presentation;

	LaunchGroupPresentation(IDebugModelPresentation presentation) {
		this.presentation = presentation;
	}

	@Override
	public Image getImage(ILaunchGroup model) {
		return this.presentation.getImage(model);
	}

	@Override
	public String getText(ILaunchGroup model) {
		return this.presentation.getText(model);
	}

	@Override
	public StyledString getStyledText(ILaunchGroup model) {
		return new StyledString(this.getText(model));
	}

}
