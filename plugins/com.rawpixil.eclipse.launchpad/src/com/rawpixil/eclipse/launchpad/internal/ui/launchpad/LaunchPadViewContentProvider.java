package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import java.util.List;

import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationRepository;
import com.rawpixil.eclipse.launchpad.core.ILaunchPad;
import com.rawpixil.eclipse.launchpad.core.IPredicate;
import com.rawpixil.eclipse.launchpad.internal.util.Assert;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

/**
 *
 * @author Willy du Preez
 *
 */
public class LaunchPadViewContentProvider implements ITreeContentProvider {

	private IExtendedLaunchConfigurationRepository repository;
	private Optional<IPredicate<IExtendedLaunchConfiguration>> filter;
	private ILaunchPad launchpad;

	public LaunchPadViewContentProvider(IExtendedLaunchConfigurationRepository repository,
			ILaunchPad launchpad) {
		this(repository, Optional.<IPredicate<IExtendedLaunchConfiguration>>empty(), launchpad);
	}

	public LaunchPadViewContentProvider(
			IExtendedLaunchConfigurationRepository repository,
			Optional<IPredicate<IExtendedLaunchConfiguration>> filter,
			ILaunchPad launchpad) {

		Assert.notNull(repository, "Repository cannot be null");
		Assert.notNull(filter, "Filter cannot be null");
		Assert.notNull(launchpad, "Launch pad cannot be null");

		this.repository = repository;
		this.filter = filter;
		this.launchpad = launchpad;
	}

	public void setFilter(Optional<IPredicate<IExtendedLaunchConfiguration>> filter) {
		Assert.notNull(filter, "Filter cannot be null");
		this.filter = filter;
	}

	@Override
	public void dispose() {
		this.repository = null;
		this.filter = null;
		this.launchpad = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof IViewSite) {
			return getConfigurations().toArray();
		}
		return getChildren(inputElement);
	}

	private List<IExtendedLaunchConfiguration> getConfigurations() {
		if (this.filter.isPresent()) {
			return this.repository.list(filter.get());
		}
		else {
			return this.repository.list();
		}
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IExtendedLaunchConfiguration) {
			List<IProcess> processes = this.launchpad.getProcesses((IExtendedLaunchConfiguration) parentElement);
			return processes.toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IExtendedLaunchConfiguration) {
			return !this.launchpad.getProcesses((IExtendedLaunchConfiguration) element).isEmpty();
		}
		return false;
	}

}
