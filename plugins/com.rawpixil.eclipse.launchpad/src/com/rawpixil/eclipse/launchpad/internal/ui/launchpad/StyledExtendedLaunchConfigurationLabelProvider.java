package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunch;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchesListener;
import com.rawpixil.eclipse.launchpad.internal.core.extended.ExtendedLauncherProvider;
import com.rawpixil.eclipse.launchpad.internal.ui.IRefreshable;
import com.rawpixil.eclipse.launchpad.internal.util.Assert;
import com.rawpixil.eclipse.launchpad.internal.util.Log;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public class StyledExtendedLaunchConfigurationLabelProvider extends StyledCellLabelProvider {

	private ExtendedLaunchConfigurationLabelProvider delegate;
	private ExtendedLaunchesStatusTracker tracker;
	private IRefreshable refreshable;

	public StyledExtendedLaunchConfigurationLabelProvider(IRefreshable refreshable) {
		Assert.notNull(refreshable, "Refreshable cannot be null.");

		this.delegate = new ExtendedLaunchConfigurationLabelProvider();
		this.tracker = new ExtendedLaunchesStatusTracker();
		this.refreshable = refreshable;

		ExtendedLauncherProvider.INSTANCE.get().addExtendedLaunchesListener(this.tracker);
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
		Optional<IExtendedLaunch> tracked = this.tracker.findExtendedLaunch(extended);
		if (tracked.isPresent()) {
			ILaunch launch = tracked.get().getLaunch();
			String base = this.delegate.getText(extended) + "  ";
			String decoration;
			if (launch.isTerminated()) {
				decoration = "[Terminated]";
			}
			else {
				decoration = "[Running, Processes: " + launch.getProcesses().length + "]";
			}
			StyledString styled = new StyledString(base);
			styled.append(decoration, StyledString.DECORATIONS_STYLER);
			return styled;
		}
		else {
			return new StyledString(this.delegate.getText(extended));
		}
	}

	private Image getImage(Object element) {
		return this.delegate.getImage(element);
	}

	private String getText(Object element) {
		return this.delegate.getText(element);
	}

	@Override
	public void dispose() {
		ExtendedLauncherProvider.INSTANCE.get().removeExtendedLaunchesListener(this.tracker);
		this.tracker.dispose();
		super.dispose();
	}

	// TODO Match multiple launches to configuration
	private class ExtendedLaunchesStatusTracker implements IExtendedLaunchesListener {

		private List<IExtendedLaunch> tracked;

		public ExtendedLaunchesStatusTracker() {
			this.tracked = Collections.synchronizedList(new ArrayList<IExtendedLaunch>());
		}

		@Override
		public void extendedLaunchesAdded(List<IExtendedLaunch> launches) {
			this.tracked.addAll(launches);
			Log.log("Styled launches tracker: extendedLaunchesAdded");
			StyledExtendedLaunchConfigurationLabelProvider.this.refreshable.refresh();
		}

		@Override
		public void extendedLaunchesChanged(List<IExtendedLaunch> launches) {
			// TODO Auto-generated method stub
			Log.log("Styled launches tracker: extendedLaunchesChanged");
			StyledExtendedLaunchConfigurationLabelProvider.this.refreshable.refresh();
		}

		@Override
		public void extendedLaunchesTerminated(List<IExtendedLaunch> launches) {
			// TODO Auto-generated method stub
			Log.log("Styled launches tracker: extendedLaunchesTerminated");
			StyledExtendedLaunchConfigurationLabelProvider.this.refreshable.refresh();
		}

		@Override
		public void extendedLaunchesRemoved(List<IExtendedLaunch> launches) {
			// TODO Better way to remove ... perhaps implement equals method in extended launch
			Log.log("Styled launches tracker: extendedLaunchesRemoved");
			this.tracked.removeAll(launches);
//			synchronized (this.tracked) {
//				Iterator<IExtendedLaunch> i = this.tracked.iterator();
//				while (i.hasNext()) {
//					IExtendedLaunch existing = i.next();
//					for (IExtendedLaunch launch : launches) {
//						if (existing.getLaunch().equals(launch.getLaunch())) {
//							Log.log("Removing tracked launch");
//							this.tracked.remove(existing);
//						}
//					}
//				}
//			}
			StyledExtendedLaunchConfigurationLabelProvider.this.refreshable.refresh();
		}

		public Optional<IExtendedLaunch> findExtendedLaunch(IExtendedLaunchConfiguration extended) {
			synchronized (this.tracked) {
				Iterator<IExtendedLaunch> i = this.tracked.iterator();
				while (i.hasNext()) {
					IExtendedLaunch launch = i.next();
					if (launch.getExtendedLaunchConfiguration().equals(extended)) {
						return Optional.of(launch);
					}
				}
				return Optional.empty();
			}
		}

		public void dispose() {
			this.tracked.clear();
			this.tracked = null;
		}

	}

}
