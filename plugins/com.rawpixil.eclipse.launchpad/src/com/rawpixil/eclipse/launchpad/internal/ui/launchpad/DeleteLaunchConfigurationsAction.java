package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.message.Messages;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelection;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelectionAction;
import com.rawpixil.eclipse.launchpad.internal.util.Dialogs;
import com.rawpixil.eclipse.launchpad.internal.util.Images;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public class DeleteLaunchConfigurationsAction extends StructuredSelectionAction {

	private static final String TEXT = Messages.delete_launch_configuration;
	private static final ImageDescriptor ICON = Images.getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE);

	public DeleteLaunchConfigurationsAction() {
		this.setText(TEXT);
		this.setImageDescriptor(ICON);
		this.setAccelerator(SWT.DEL);
	}

	@Override
	protected void handleSelection(StructuredSelection selection) {
		if (selection.containsSingleItemOfType(IExtendedLaunchConfiguration.class)) {
			this.deleteLaunchConfiguration(selection.getSingleItemOfType(IExtendedLaunchConfiguration.class).get());
		}
		else if (selection.containsMultipleItemsOfType(IExtendedLaunchConfiguration.class)) {
			this.deleteLaunchConfigurations(selection.getMultipleItemsOfType(IExtendedLaunchConfiguration.class).get());
		}
	}

	@Override
	protected void handleSelectionChanged(Optional<StructuredSelection> selection) {
		this.setEnabled(selection.isPresent() &&
				(selection.get().containsMultipleItemsOfType(IExtendedLaunchConfiguration.class)
						|| selection.get().containsSingleItemOfType(IExtendedLaunchConfiguration.class)));
	}

	private void deleteLaunchConfiguration(IExtendedLaunchConfiguration extended) {
		boolean confirmed = Dialogs.confirm(
				Messages.delete_launch_configuration_dialog_header,
				Messages.delete_launch_configuration_dialog_description);
		if (confirmed) {
			try {
				extended.getLaunchConfiguration().delete();
			} catch (CoreException e) {
				Dialogs.error("Error", "Eclipse failed to delete the configuration from underlying storage.");
			}
		}
	}

	private void deleteLaunchConfigurations(List<IExtendedLaunchConfiguration> items) {
		boolean confirmed = Dialogs.confirm(
				Messages.delete_launch_configurations_dialog_header,
				Messages.delete_launch_configurations_dialog_description);
		if (confirmed) {
			try {
				for (IExtendedLaunchConfiguration extended : items) {
					extended.getLaunchConfiguration().delete();
				}
			} catch (CoreException e) {
				Dialogs.error("Error", "Eclipse failed to delete the configurations from underlying storage.");
			}
		}
	}

}
