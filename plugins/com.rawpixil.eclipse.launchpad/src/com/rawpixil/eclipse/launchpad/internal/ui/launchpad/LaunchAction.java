package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.ILaunchGroup;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationWorkingCopy;
import com.rawpixil.eclipse.launchpad.internal.core.extended.ExtendedLaunchConfigurationRepositoryProvider;
import com.rawpixil.eclipse.launchpad.internal.core.extended.LaunchPadProvider;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelection;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelectionAction;
import com.rawpixil.eclipse.launchpad.internal.util.ELF;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public class LaunchAction extends StructuredSelectionAction {

	private String category;
	private String mode;

	public LaunchAction(ILaunchGroup group) {
		this.setId(group.getIdentifier());
		this.setText(group.getLabel());
		this.setImageDescriptor(group.getImageDescriptor());
		// TODO: Check XSD if mode can be null / omitted
		this.setMode(group.getMode());
		this.setCategory(group.getCategory());
		this.setEnabled(false);
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		// TODO: Check XSD if mode can be null / omitted
		this.mode = mode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	protected void handleSelection(StructuredSelection selection) {
		Optional<IExtendedLaunchConfiguration> item = selection.getSingleItemOfType(IExtendedLaunchConfiguration.class);
		if (item.isPresent()) {
			String mode = item.get().getDefaultLaunchMode().get();
			// TODO so many null checks ...
			if (!mode.equals(this.mode)) {
				IExtendedLaunchConfigurationWorkingCopy copy = item.get().getWorkingCopy();
				copy.setDefaultLaunchMode(Optional.of(this.mode));
				ExtendedLaunchConfigurationRepositoryProvider.INSTANCE.get().save(copy);
			}
			LaunchPadProvider.INSTANCE.get().launch(item.get());
		}
	}

	@Override
	protected void handleSelectionChanged(Optional<StructuredSelection> selection) {
		boolean enabled = false;
		if (selection.isPresent()) {
			Optional<IExtendedLaunchConfiguration> extended = selection.get()
					.getSingleItemOfType(IExtendedLaunchConfiguration.class);
			if (extended.isPresent()) {
				ILaunchConfiguration config = extended.get().getLaunchConfiguration();
				Optional<ILaunchGroup> group = ELF.getLaunchGroup(config, this.mode);
				if (group.isPresent()
						&& this.getId() != null
						&& this.getId().equals(group.get().getIdentifier())) {
					enabled = true;
				}

			}
		}
		this.setEnabled(enabled);
	}

}
