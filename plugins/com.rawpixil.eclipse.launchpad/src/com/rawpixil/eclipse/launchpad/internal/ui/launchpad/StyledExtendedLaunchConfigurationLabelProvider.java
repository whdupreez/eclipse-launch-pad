package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import java.util.List;

import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunch;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.core.extended.LaunchPadProvider;

public class StyledExtendedLaunchConfigurationLabelProvider extends StyledCellLabelProvider implements ILabelProvider {

	private ExtendedLaunchConfigurationLabelProvider delegate;

	public StyledExtendedLaunchConfigurationLabelProvider() {
		this.delegate = new ExtendedLaunchConfigurationLabelProvider();
	}

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof IExtendedLaunchConfiguration) {
			StyledString styledString = this.styleText((IExtendedLaunchConfiguration) element);

			cell.setText(styledString.getString());
			cell.setStyleRanges(styledString.getStyleRanges());
			cell.setImage(this.delegate.getImage(element));
		} else {
			cell.setText(this.getText(element));
			cell.setImage(this.getImage(element));
		}
		super.update(cell);
	}

	private StyledString styleText(IExtendedLaunchConfiguration extended) {
		List<IExtendedLaunch> launches = LaunchPadProvider.INSTANCE.get().getExtendedLaunches(extended);
		if (!launches.isEmpty()) {
			String base = this.delegate.getText(extended) + "  ";
			String decoration = decorate(status(launches));
			StyledString styled = new StyledString(base);
			styled.append(decoration, StyledString.DECORATIONS_STYLER);
			return styled;
		}
		else {
			return new StyledString(this.delegate.getText(extended));
		}
	}

	private String decorate(final LaunchStatusStruct status) {
		String decoration;
		if (status.active) {
			if (status.processes > 0) {
				decoration = "[Launched, Processes: " + status.processes + "]";
			} else {
				decoration = "[Launching ...]";
			}
		}
		else {
			decoration = "[Terminated]";
		}
		return decoration;
	}

	private LaunchStatusStruct status(List<IExtendedLaunch> launches) {
		final LaunchStatusStruct status = new LaunchStatusStruct();
		for (IExtendedLaunch launch : launches) {
			if (!launch.getLaunch().isTerminated()) {
				status.active = true;
				for (IProcess process : launch.getLaunch().getProcesses()) {
					if (!process.isTerminated()) {
						status.processes++;
					}
				}
			}
		}
		return status;
	}

	@Override
	public Image getImage(Object element) {
		return this.delegate.getImage(element);
	}

	@Override
	public String getText(Object element) {
		return this.delegate.getText(element);
	}

	private class LaunchStatusStruct {
		private boolean active;
		private int processes;
	}

}
