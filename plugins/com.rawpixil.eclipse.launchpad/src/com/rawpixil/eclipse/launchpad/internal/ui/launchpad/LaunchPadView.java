package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunch;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationListener;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationRepository;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchesListener;
import com.rawpixil.eclipse.launchpad.core.ILaunchPad;
import com.rawpixil.eclipse.launchpad.core.IPredicate;
import com.rawpixil.eclipse.launchpad.core.IPreferences;
import com.rawpixil.eclipse.launchpad.internal.core.extended.ExtendedLaunchConfigurationRepositoryProvider;
import com.rawpixil.eclipse.launchpad.internal.core.extended.LaunchPadProvider;
import com.rawpixil.eclipse.launchpad.internal.core.extended.filter.Predicates;
import com.rawpixil.eclipse.launchpad.internal.core.preferences.Preference;
import com.rawpixil.eclipse.launchpad.internal.core.preferences.PreferencesProvider;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelection;
import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelectionAction;
import com.rawpixil.eclipse.launchpad.internal.ui.presentation.LaunchPadModelPresentationProvider;
import com.rawpixil.eclipse.launchpad.internal.util.Displays;
import com.rawpixil.eclipse.launchpad.internal.util.Log;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public class LaunchPadView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.rawpixil.eclipse.launchpad.internal.ui.launchpad.LaunchPadView";

	private TreeViewer viewer;
	private LaunchPadViewContentProvider viewerContentProvider;

	private ILaunchPad launchpad;
	private IExtendedLaunchConfigurationRepository repository;

	private IDoubleClickListener lstDoubleClick;
	private IPropertyChangeListener lstPreferencesChange;
	private RefreshViewListener lstRefreshView;

	private Action aOrganizeFavorites;
	private Action aFilterFavorites;
	private StructuredSelectionAction aAddToFavorites;
	private StructuredSelectionAction aRemoveFromFavorites;
	private StructuredSelectionAction aDeleteLaunchConfigurations;
	private StructuredSelectionAction aRenameLaunchConfiguration;
	private StructuredSelectionAction aTerminateLaunch;
	private StructuredSelectionAction aTerminateAndRelaunch;
	private StructuredSelectionAction aLaunchAsDefaultGroup;
	private List<StructuredSelectionAction> aLaunchAsGroup;

	public LaunchPadView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		this.launchpad = LaunchPadProvider.INSTANCE.get();
		this.repository = ExtendedLaunchConfigurationRepositoryProvider.INSTANCE.get();

		// Create the TreeViewer.
		PatternFilter filter = new PatternFilter();
		filter.setIncludeLeadingWildcard(true);
		FilteredTree tree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, filter, true);
		this.viewer = tree.getViewer();

		// Configure the TreeViewer.
		this.viewerContentProvider = new LaunchPadViewContentProvider(this.repository, this.launchpad);
		this.viewer.setContentProvider(this.viewerContentProvider);
//		this.viewer.setLabelProvider(new ExtendedLaunchConfigurationLabelProvider());
		this.viewer.setLabelProvider(new LaunchPadViewLabelProvider(LaunchPadModelPresentationProvider.INSTANCE.get()));
		this.viewer.setInput(this.getViewSite());
//		viewer.addTreeListener(new ITreeViewerListener() {
//			@Override
//			public void treeExpanded(TreeExpansionEvent event) {
//				System.out.println("Expansion event (expand): " + event.toString());
//				Object element = event.getElement();
//				if(element instanceof IExpandable) {
//					((IExpandable) element).setExpanded(true);
//				}
//			}
//			@Override
//			public void treeCollapsed(TreeExpansionEvent event) {
//				System.out.println("Expansion event (collapse): " + event.toString());
//				Object element = event.getElement();
//				if(element instanceof IExpandable) {
//					((IExpandable) element).setExpanded(false);
//				}
//			}
//		});
//		// Create the help context id for the viewer's control
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.rawpixil.eclipse.launchpad.viewer");
		this.getSite().setSelectionProvider(this.viewer);
		this.makeActions();
		this.hookContextMenu();
		this.hookDoubleClickAction();
		this.contributeToActionBars();
		this.registerListeners();
		this.refresh();
	}

	private void makeActions() {
		this.aDeleteLaunchConfigurations = new DeleteLaunchConfigurationsAction();
		this.aRenameLaunchConfiguration = new RenameLaunchConfigurationAction();
		this.aTerminateLaunch = new TerminateLaunchAction();
		this.aTerminateAndRelaunch = new TerminateAndRelaunchAction();
		this.aLaunchAsDefaultGroup = new DefaultLaunchAction();
		this.aOrganizeFavorites = new OrganizeFavoritesAction();
		this.aFilterFavorites = new FilterFavoritesAction();
		this.aAddToFavorites = new AddToFavoritesAction();
		this.aRemoveFromFavorites = new RemoveFromFavoritesAction();
		this.aLaunchAsGroup = LaunchAction.createAllAvailable();
	}

	private void hookContextMenu() {
		MenuManager manager = new MenuManager("#PopupMenu");
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				LaunchPadView.this.fillContextMenu(manager);
			}
		});
		Menu menu = manager.createContextMenu(this.viewer.getControl());
		this.viewer.getControl().setMenu(menu);
		this.getSite().registerContextMenu(manager, this.viewer);
	}

	private void fillContextMenu(IMenuManager manager) {
		StructuredSelection selection = new StructuredSelection((IStructuredSelection) this.viewer.getSelection());
		// Duplication warranted by ease of comprehension
		// of what the menu will look like for different
		// item selections.
		if (selection.containsOnlyItemsOfType(IExtendedLaunchConfiguration.class)) {
			manager.add(this.aRenameLaunchConfiguration);
			manager.add(this.aDeleteLaunchConfigurations);
			manager.add(new Separator());
			manager.add(this.aAddToFavorites);
			manager.add(this.aRemoveFromFavorites);
		}
//		manager.add(new Separator());
//		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
//		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void hookDoubleClickAction() {
		this.lstDoubleClick = new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				LaunchPadView.this.aLaunchAsDefaultGroup.run();
			}
		};
		this.viewer.addDoubleClickListener(this.lstDoubleClick);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(this.aOrganizeFavorites);
		manager.add(new Separator());
//		manager.add(action2);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
//		manager.add(this.aLaunchAsDefaultGroup);
		for (Action action : this.aLaunchAsGroup) {
			manager.add(action);
		}
		manager.add(new Separator());
		manager.add(this.aTerminateLaunch);
		manager.add(this.aTerminateAndRelaunch);
		manager.add(new Separator());
		manager.add(this.aFilterFavorites);
//		manager.add(action2);
		manager.add(new Separator());
//		drillDownAdapter.addNavigationActions(manager);
	}

	private void registerListeners() {
		this.lstRefreshView = new RefreshViewListener();
		this.repository.addExtendedLaunchConfigurationListener(this.lstRefreshView);
		this.launchpad.addExtendedLaunchesListener(this.lstRefreshView);
		this.lstPreferencesChange = new PreferencesChangeListener();
		PreferencesProvider.INSTANCE.get().addPropertyChangeListener(this.lstPreferencesChange);
		this.viewer.addSelectionChangedListener(this.aAddToFavorites);
		this.viewer.addSelectionChangedListener(this.aRemoveFromFavorites);
		this.viewer.addSelectionChangedListener(this.aLaunchAsDefaultGroup);
		this.viewer.addSelectionChangedListener(this.aDeleteLaunchConfigurations);
		this.viewer.addSelectionChangedListener(this.aRenameLaunchConfiguration);
		this.viewer.addSelectionChangedListener(this.aTerminateLaunch);
		this.viewer.addSelectionChangedListener(this.aTerminateAndRelaunch);
		for (StructuredSelectionAction action : this.aLaunchAsGroup) {
			this.viewer.addSelectionChangedListener(action);
		}
	}

	@Override
	public void setFocus() {
		this.viewer.getControl().setFocus();
	}

	private void refresh() {
		Displays.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				LaunchPadView.this.viewer.refresh();
			}
		});
	}

	@Override
	public void dispose() {
		this.viewer.removeDoubleClickListener(this.lstDoubleClick);
		this.viewer.removeSelectionChangedListener(this.aAddToFavorites);
		this.viewer.removeSelectionChangedListener(this.aRemoveFromFavorites);
		this.viewer.removeSelectionChangedListener(this.aLaunchAsDefaultGroup);
		this.viewer.removeSelectionChangedListener(this.aDeleteLaunchConfigurations);
		this.viewer.removeSelectionChangedListener(this.aRenameLaunchConfiguration);
		this.viewer.removeSelectionChangedListener(this.aTerminateLaunch);
		this.viewer.removeSelectionChangedListener(this.aTerminateAndRelaunch);
		for (StructuredSelectionAction action : this.aLaunchAsGroup) {
			this.viewer.removeSelectionChangedListener(action);
		}
		this.lstDoubleClick = null;
		this.repository.removeExtendedLaunchConfigurationListener(this.lstRefreshView);
		this.launchpad.removeExtendedLaunchesListener(this.lstRefreshView);
		this.lstRefreshView = null;
		PreferencesProvider.INSTANCE.get().removePropertyChangeListener(this.lstPreferencesChange);
		this.lstPreferencesChange = null;
		this.repository = null;
		this.launchpad = null;
		super.dispose();
	}

	private class RefreshViewListener implements IExtendedLaunchConfigurationListener, IExtendedLaunchesListener {

		@Override
		public void extendedLaunchConfigurationAdded(IExtendedLaunchConfiguration extended) {
			Log.log("LaunchPadView.RefreshViewListener.extendedLaunchConfigurationAdded");
			LaunchPadView.this.refresh();
		}

		@Override
		public void extendedLaunchConfigurationChanged(IExtendedLaunchConfiguration extended) {
			Log.log("LaunchPadView.RefreshViewListener.extendedLaunchConfigurationChanged");
			LaunchPadView.this.aAddToFavorites.refresh();
			LaunchPadView.this.aRemoveFromFavorites.refresh();
			LaunchPadView.this.refresh();
		}

		@Override
		public void extendedLaunchConfigurationRemoved(IExtendedLaunchConfiguration extended) {
			Log.log("LaunchPadView.RefreshViewListener.extendedLaunchConfigurationRemoved");
			LaunchPadView.this.refresh();
		}

		@Override
		public void extendedLaunchesAdded(List<IExtendedLaunch> launches) {
			Log.log("LaunchPadView.RefreshViewListener.extendedLaunchesAdded");
			LaunchPadView.this.aTerminateLaunch.refresh();
			LaunchPadView.this.refresh();
		}

		@Override
		public void extendedLaunchesChanged(List<IExtendedLaunch> launches) {
			Log.log("LaunchPadView.RefreshViewListener.extendedLaunchesAdded");
			LaunchPadView.this.aTerminateLaunch.refresh();
			LaunchPadView.this.aTerminateAndRelaunch.refresh();
			LaunchPadView.this.refresh();
		}

		@Override
		public void extendedLaunchesTerminated(List<IExtendedLaunch> launches) {
			Log.log("LaunchPadView.RefreshViewListener.extendedLaunchesAdded");
			LaunchPadView.this.aTerminateLaunch.refresh();
			LaunchPadView.this.aTerminateAndRelaunch.refresh();
			LaunchPadView.this.refresh();
		}

		@Override
		public void extendedLaunchesRemoved(List<IExtendedLaunch> launches) {
			Log.log("LaunchPadView.RefreshViewListener.extendedLaunchesAdded");
			LaunchPadView.this.refresh();
		}

	}

	private class PreferencesChangeListener implements IPropertyChangeListener {

		private IPreferences preferences;
		private boolean filterFavorites;

		public PreferencesChangeListener() {
			this.preferences = PreferencesProvider.INSTANCE.get();
			this.filterFavorites = preferences.isEnbaled(Preference.LAUNCHER_VIEW_FAVORITES_FILTER);
			LaunchPadView.this.viewerContentProvider.setFilter(this.createFilter());
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			boolean dirty = false;
			synchronized (this) {
				if (Preference.LAUNCHER_VIEW_FAVORITES_FILTER.getName().equals(event.getProperty())) {
					this.filterFavorites = preferences.isEnbaled(Preference.LAUNCHER_VIEW_FAVORITES_FILTER);
					dirty = true;
				}
			}
			if (dirty) {
				Optional<IPredicate<IExtendedLaunchConfiguration>> filter = createFilter();
				LaunchPadView.this.viewerContentProvider.setFilter(filter);
				LaunchPadView.this.refresh();
			}
		}

		private Optional<IPredicate<IExtendedLaunchConfiguration>> createFilter() {
			if (this.filterFavorites) {
				return Optional.<IPredicate<IExtendedLaunchConfiguration>>of(Predicates.favorites());
			}
			return Optional.empty();
		}

	}

}
