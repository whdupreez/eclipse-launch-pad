package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.core.extended.LaunchPadProvider;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelection;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelectionAction;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public class DefaultLaunchAction extends StructuredSelectionAction {

	public DefaultLaunchAction() {
	}

	@Override
	protected void handleSelection(StructuredSelection selection) {
		Optional<IExtendedLaunchConfiguration> item = selection.getSingleItemOfType(IExtendedLaunchConfiguration.class);
		if (item.isPresent() && item.get().getDefaultLaunchMode().isPresent()) {
			LaunchPadProvider.INSTANCE.get().launch(item.get());
		}
	}

	@Override
	protected void handleSelectionChanged(Optional<StructuredSelection> selection) {
	}

}
