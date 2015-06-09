package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.ui.ISharedImages;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.core.extended.LaunchPadProvider;
import com.rawpixil.eclipse.launchpad.internal.message.Messages;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelection;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelectionAction;
import com.rawpixil.eclipse.launchpad.internal.util.Dialogs;
import com.rawpixil.eclipse.launchpad.internal.util.Images;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public class TerminateLaunchAction extends StructuredSelectionAction {

	public TerminateLaunchAction() {
		this.setText(Messages.terminate_launch_action);
		this.setImageDescriptor(Images.getImageDescriptor(ISharedImages.IMG_ELCL_STOP));
		this.setEnabled(false);
	}

	@Override
	protected void handleSelection(StructuredSelection selection) {
		Optional<IExtendedLaunchConfiguration> config = selection.getSingleItemOfType(IExtendedLaunchConfiguration.class);
		if (config.isPresent()) {
			List<IProcess> processes = LaunchPadProvider.INSTANCE.get().getProcesses(config.get());
			for (IProcess process : processes) {
				if (process.canTerminate() && !process.isTerminated()) {
					try {
						process.terminate();
					} catch (DebugException e) {
						Dialogs.error(Messages.terminate_launch_dialog_error_title,
								Messages.terminate_launch_dialog_error_description);
					}
				}
			}
		}
	}

	@Override
	protected void handleSelectionChanged(Optional<StructuredSelection> selection) {
		boolean enabled = false;
		if (selection.isPresent()) {
			Optional<IExtendedLaunchConfiguration> config = selection.get().getSingleItemOfType(IExtendedLaunchConfiguration.class);
			if (config.isPresent()) {
				List<IProcess> processes = LaunchPadProvider.INSTANCE.get().getProcesses(config.get());
				for (IProcess process : processes) {
					if (!process.isTerminated()) {
						enabled = true;
						break;
					}
				}
			}
		}
		this.setEnabled(enabled);
	}

}
