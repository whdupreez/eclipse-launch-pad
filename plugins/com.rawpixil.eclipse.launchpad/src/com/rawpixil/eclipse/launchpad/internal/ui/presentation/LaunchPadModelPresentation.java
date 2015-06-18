package com.rawpixil.eclipse.launchpad.internal.ui.presentation;

import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.ILaunchGroup;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.ui.IPresentation;
import com.rawpixil.eclipse.launchpad.internal.util.Assert;
import com.rawpixil.eclipse.launchpad.internal.util.Images;

public class LaunchPadModelPresentation implements IPresentation<Object> {

	private ExtendedLaunchConfigurationPresentation extended;
	private LaunchGroupPresentation group;
	private ProcessPresentation process;
	private LaunchConfigurationTypePresentation type;

	LaunchPadModelPresentation() {
		IDebugModelPresentation debug = DebugUITools.newDebugModelPresentation();
		this.extended = new ExtendedLaunchConfigurationPresentation(debug);
		this.group = new LaunchGroupPresentation(debug);
		this.process = new ProcessPresentation(debug);
		this.type = new LaunchConfigurationTypePresentation(debug);
	}

	@Override
	public Image getImage(Object model) {
		Assert.notNull(model, "Model cannot be null");
		if (model instanceof IExtendedLaunchConfiguration) {
			return this.extended.getImage((IExtendedLaunchConfiguration) model);
		}
		else if (model instanceof ILaunchGroup) {
			return this.group.getImage((ILaunchGroup) model);
		}
		else if (model instanceof IProcess) {
			return this.process.getImage((IProcess) model);
		}
		else if (model instanceof ILaunchConfigurationType) {
			return this.type.getImage((ILaunchConfigurationType) model);
		}
		return Images.getMissingImage();
	}

	@Override
	public String getText(Object model) {
		Assert.notNull(model, "Model cannot be null");
		if (model instanceof IExtendedLaunchConfiguration) {
			return this.extended.getText((IExtendedLaunchConfiguration) model);
		}
		else if (model instanceof ILaunchGroup) {
			return this.group.getText((ILaunchGroup) model);
		}
		else if (model instanceof IProcess) {
			return this.process.getText((IProcess) model);
		}
		else if (model instanceof ILaunchConfigurationType) {
			return this.type.getText((ILaunchConfigurationType) model);
		}
		return "Unknown Model";
	}

	@Override
	public StyledString getStyledText(Object model) {
		Assert.notNull(model, "Model cannot be null");
		if (model instanceof IExtendedLaunchConfiguration) {
			return this.extended.getStyledText((IExtendedLaunchConfiguration) model);
		}
		else if (model instanceof ILaunchGroup) {
			return this.group.getStyledText((ILaunchGroup) model);
		}
		else if (model instanceof IProcess) {
			return this.process.getStyledText((IProcess) model);
		}
		else if (model instanceof ILaunchConfigurationType) {
			return this.type.getStyledText((ILaunchConfigurationType) model);
		}
		return new StyledString("Unknown Model");
	}

}
