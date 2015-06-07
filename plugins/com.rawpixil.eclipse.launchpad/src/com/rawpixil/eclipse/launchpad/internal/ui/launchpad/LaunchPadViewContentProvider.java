package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationRepository;
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

	public LaunchPadViewContentProvider(IExtendedLaunchConfigurationRepository repository) {
		this(repository, Optional.<IPredicate<IExtendedLaunchConfiguration>>empty());
	}

	public LaunchPadViewContentProvider(
			IExtendedLaunchConfigurationRepository repository,
			Optional<IPredicate<IExtendedLaunchConfiguration>> filter) {

		Assert.notNull(repository, "Repository cannot be null");
		Assert.notNull(filter, "Filter cannot be null");

		this.repository = repository;
		this.filter = filter;
	}

	@Override
	public void dispose() {
		this.repository = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof IViewSite) {
			if (this.filter.isPresent()) {
				return this.repository.list(filter.get()).toArray();
			}
			else {
				return this.repository.list().toArray();
			}
		}
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}

	public void setFilter(Optional<IPredicate<IExtendedLaunchConfiguration>> filter) {
		Assert.notNull(filter, "Filter cannot be null");
		this.filter = filter;
	}

}
