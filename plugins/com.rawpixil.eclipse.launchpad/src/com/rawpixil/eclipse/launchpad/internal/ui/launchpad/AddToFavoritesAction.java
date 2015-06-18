package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import java.util.List;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationRepository;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationWorkingCopy;
import com.rawpixil.eclipse.launchpad.internal.core.extended.ExtendedLaunchConfigurationRepositoryProvider;
import com.rawpixil.eclipse.launchpad.internal.message.Messages;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelection;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelectionAction;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public class AddToFavoritesAction extends StructuredSelectionAction {

	public AddToFavoritesAction() {
		this.setText(Messages.add_to_favorites_action);
	}

	@Override
	protected void handleSelection(StructuredSelection selection) {
		IExtendedLaunchConfigurationRepository repo = ExtendedLaunchConfigurationRepositoryProvider.INSTANCE.get();
		List<IExtendedLaunchConfiguration> configs = selection.getItemsOfType(IExtendedLaunchConfiguration.class);
		for (IExtendedLaunchConfiguration config : configs) {
			if (!config.isFavorite()) {
				IExtendedLaunchConfigurationWorkingCopy copy = config.getWorkingCopy();
				copy.setFavorite(true);
				repo.save(copy);
			}
		}
	}

	@Override
	protected void handleSelectionChanged(Optional<StructuredSelection> selection) {
		boolean enabled = false;
		if (selection.isPresent()) {
			Optional<List<IExtendedLaunchConfiguration>> configs =
					selection.get().getOnlyItemsOfType(IExtendedLaunchConfiguration.class);
			if (configs.isPresent()) {
				for (IExtendedLaunchConfiguration config : configs.get()) {
					if (!config.isFavorite()) {
						enabled = true;
					}
				}
			}
		}
		this.setEnabled(enabled);
	}

}
