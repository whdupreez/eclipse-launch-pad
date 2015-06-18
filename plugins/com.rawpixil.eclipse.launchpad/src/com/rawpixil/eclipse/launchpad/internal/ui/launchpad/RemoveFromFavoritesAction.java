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

public class RemoveFromFavoritesAction extends StructuredSelectionAction {

	public RemoveFromFavoritesAction() {
		this.setText(Messages.remove_from_favorites_action);
	}

	@Override
	protected void handleSelection(StructuredSelection selection) {
		IExtendedLaunchConfigurationRepository repo = ExtendedLaunchConfigurationRepositoryProvider.INSTANCE.get();
		List<IExtendedLaunchConfiguration> configs = selection.getItemsOfType(IExtendedLaunchConfiguration.class);
		for (IExtendedLaunchConfiguration config : configs) {
			if (config.isFavorite()) {
				IExtendedLaunchConfigurationWorkingCopy copy = config.getWorkingCopy();
				copy.setFavorite(false);
				repo.save(copy);
			}
		}
	}

	@Override
	protected void handleSelectionChanged(Optional<StructuredSelection> selection) {
		boolean enabled = false;
		if (selection.isPresent()) {
			if (selection.get().containsSingleItemOfType(IExtendedLaunchConfiguration.class)) {
				IExtendedLaunchConfiguration config = selection
						.get().getSingleItemOfType(IExtendedLaunchConfiguration.class).get();
				enabled = config.isFavorite();
			}
			else if (selection.get().containsMultipleItemsOfType(IExtendedLaunchConfiguration.class)) {
				List<IExtendedLaunchConfiguration> configs =
						selection.get().getMultipleItemsOfType(IExtendedLaunchConfiguration.class).get();
				enabled = true;
				for (IExtendedLaunchConfiguration config : configs) {
					if (!config.isFavorite()) {
						enabled = false;
					}
				}
			}
		}
		this.setEnabled(enabled);
	}

}
