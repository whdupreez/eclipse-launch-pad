package com.rawpixil.eclipse.launchpad.internal.ui.presentation;

import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import com.rawpixil.eclipse.launchpad.internal.ui.IPresentation;

public class ProcessPresentation implements IPresentation<IProcess> {

	private IDebugModelPresentation presentation;

	ProcessPresentation(IDebugModelPresentation presentation) {
		this.presentation = presentation;
	}

	@Override
	public Image getImage(IProcess model) {
		return this.presentation.getImage(model);
	}

	@Override
	public String getText(IProcess model) {
		return this.presentation.getText(model);
	}

	@Override
	public StyledString getStyledText(IProcess model) {
		return new StyledString(this.getText(model));
	}

}
