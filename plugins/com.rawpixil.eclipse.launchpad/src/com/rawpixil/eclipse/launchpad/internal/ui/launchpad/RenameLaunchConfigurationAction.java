package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.message.Messages;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelection;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelectionAction;
import com.rawpixil.eclipse.launchpad.internal.util.Dialogs;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public class RenameLaunchConfigurationAction extends StructuredSelectionAction {

	private static final String TEXT = Messages.rename_launch_configuration;

	public RenameLaunchConfigurationAction() {
		this.setText(TEXT);
//		this.setId("org.eclipse.jdt.ui.edit.text.java.rename.element");
//		this.setActionDefinitionId("org.eclipse.jdt.ui.edit.text.java.rename.element");
	}

	@Override
	protected void handleSelectionChanged(Optional<StructuredSelection> selection) {
		this.setEnabled(selection.isPresent()
				&& selection.get().containsSingleItemOfType(IExtendedLaunchConfiguration.class));
	}

	@Override
	protected void handleSelection(StructuredSelection selection) {
		Optional<IExtendedLaunchConfiguration> item = selection.getSingleItemOfType(IExtendedLaunchConfiguration.class);
		if (item.isPresent()) {
			try {
				ILaunchConfiguration config = item.get().getLaunchConfiguration();
				String initialValue = config.getName();
				Optional<String> input = Dialogs.input(
						Messages.rename_launch_configuration_dialog_header,
						Messages.rename_launch_configuration_dialog_description,
						initialValue,
						new LaunchConfigurationNameValidator(initialValue));
				if (input.isPresent() && !input.get().trim().equals(initialValue)) {
					ILaunchConfigurationWorkingCopy workingCopy = config.getWorkingCopy();
					workingCopy.rename(input.get().trim());
					workingCopy.doSave();
				}
			} catch (CoreException e) {
				Dialogs.error("Error", "Eclipse failed to delete the configuration from underlying storage.");
			}
		}
	}

}
