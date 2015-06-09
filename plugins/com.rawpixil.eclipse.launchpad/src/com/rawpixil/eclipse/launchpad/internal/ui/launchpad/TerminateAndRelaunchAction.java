package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IProcess;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.core.extended.LaunchPadProvider;
import com.rawpixil.eclipse.launchpad.internal.message.Messages;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelection;
import com.rawpixil.eclipse.launchpad.internal.util.Dialogs;
import com.rawpixil.eclipse.launchpad.internal.util.Images;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public class TerminateAndRelaunchAction extends TerminateLaunchAction {

	public TerminateAndRelaunchAction() {
		this.setText(Messages.terminate_and_relaunch_action);
		this.setImageDescriptor(Images.getImageDescriptor(Images.ICON_TERMINATE_AND_RELAUNCH));
		this.setEnabled(false);
	}

	@Override
	protected void handleSelection(StructuredSelection selection) {
		Optional<IExtendedLaunchConfiguration> config = selection.getSingleItemOfType(IExtendedLaunchConfiguration.class);
		if (config.isPresent()) {
			List<IProcess> processes = LaunchPadProvider.INSTANCE.get().getProcesses(config.get());
			boolean launch = true;
			for (IProcess process : processes) {
				if (process.canTerminate() && !process.isTerminated()) {
					try {
						process.terminate();
					} catch (DebugException e) {
						launch = false;
						Dialogs.error(Messages.terminate_launch_dialog_error_title,
								Messages.terminate_launch_dialog_error_description);
					}
				}
			}
			if (launch) {
				LaunchPadProvider.INSTANCE.get().launch(config.get());
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
