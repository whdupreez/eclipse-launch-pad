package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import org.eclipse.jface.dialogs.IInputValidator;

import com.rawpixil.eclipse.launchpad.internal.message.Messages;
import com.rawpixil.eclipse.launchpad.internal.util.Assert;
import com.rawpixil.eclipse.launchpad.internal.util.ELF;

public class LaunchConfigurationNameValidator implements IInputValidator {

	private String intialName;

	public LaunchConfigurationNameValidator(String initialName) {
		Assert.notNull(initialName, "Initial name cannot be null");
		this.intialName = initialName;
	}

	@Override
	public String isValid(String newName) {
		if (newName == null || newName.trim().isEmpty()) {
			return Messages.rename_launch_configuration_error_empty;
		}
		newName = newName.trim();
		if (!newName.trim().equals(intialName) && ELF.isExistingLaunchConfigurationName(newName.trim())) {
			return Messages.rename_launch_configuration_error_exists;
		}
		else if (!ELF.isValidLaunchConfigurationName(newName)) {
			return Messages.rename_launch_configuration_error_invalid;
		}
		else {
			return null;
		}
	}

}
