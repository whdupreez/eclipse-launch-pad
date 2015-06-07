/*******************************************************************************
 * Copyright (c) 2015 Willy du Preez
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Willy du Preez - copied and changed to work with all launch configurations
 *******************************************************************************/
package com.rawpixil.eclipse.launchpad.internal.ui.component.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationWorkingCopy;
import com.rawpixil.eclipse.launchpad.internal.core.extended.ExtendedLaunchConfigurationRepositoryProvider;
import com.rawpixil.eclipse.launchpad.internal.core.extended.filter.Predicates;
import com.rawpixil.eclipse.launchpad.internal.core.extended.function.Functions;
import com.rawpixil.eclipse.launchpad.internal.message.Messages;
import com.rawpixil.eclipse.launchpad.internal.ui.SWTFactory;
import com.rawpixil.eclipse.launchpad.internal.ui.launchpad.ExtendedLaunchConfigurationLabelProvider;
import com.rawpixil.eclipse.launchpad.internal.util.Log;
import com.rawpixil.eclipse.launchpad.internal.util.functional.Functional;

/**
 * Dialog for organizing favorite extended launch configurations.
 */
public class OrganizeFavoritesDialog extends TrayDialog {

	private TableViewer favorites;

	protected Button btnAddFavorite;
	protected Button btnRemoveFavorites;
	protected Button btnMoveUp;
	protected Button btnMoveDown;

	private List<IExtendedLaunchConfigurationWorkingCopy> copies;

	private SelectionAdapter buttonPressedListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Button button = (Button) e.widget;
			if (button == btnAddFavorite) {
				handleAddConfigButtonPressed();
			}
			else if (button == btnRemoveFavorites) {
				handleRemoveFavoritesButtonPressed();
			}
			else if (button == btnMoveUp) {
				handleMoveUpButtonPressed();
			}
			else if (button == btnMoveDown) {
				handleMoveDownButtonPressed();
			}
		}
	};

	private ISelectionChangedListener selectionChangedListener = new ISelectionChangedListener() {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			handleFavoriteSelectionChanged();
		}
	};

	private KeyListener keyPressedListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent event) {
			if (event.character == SWT.DEL && event.stateMask == 0) {
				handleRemoveFavoritesButtonPressed();
			}
		}
	};

	protected class FavoritesContentProvider implements IStructuredContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			Log.log("LaunchConfigurationContentProvider expecting !!!!!!: " + inputElement);
			return Functional.filter(OrganizeFavoritesDialog.this.copies, Predicates.favorites()).toArray();
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	public OrganizeFavoritesDialog(Shell parentShell) {
		super(parentShell);
		this.setShellStyle(this.getShellStyle() | SWT.RESIZE);
	}

	protected void handleAddConfigButtonPressed() {
		SelectFavoritesDialog dialog = new SelectFavoritesDialog(favorites.getControl().getShell(), this.copies);
		dialog.open();
		Object[] selection = dialog.getResult();
		if (selection != null) {
			for (int i = 0; i < selection.length; i++) {
				((IExtendedLaunchConfigurationWorkingCopy) selection[i]).setFavorite(true);
			}
			updateStatus();
		}
	}

	protected void handleRemoveFavoritesButtonPressed() {
		// IStructuredSelection sel =
		// (IStructuredSelection)getFavoritesTable().getSelection();
		// Iterator<?> iter = sel.iterator();
		// while (iter.hasNext()) {
		// Object config = iter.next();
		// getFavorites().remove(config);
		// }
		// getFavoritesTable().refresh();
	}

	protected void handleMoveUpButtonPressed() {
		handleMove(-1);
	}

	protected void handleMoveDownButtonPressed() {
		handleMove(1);
	}

	/**
	 * Handles moving a favorite up or down the listing
	 *
	 * @param direction
	 *            the direction to make the move (up or down)
	 */
	protected void handleMove(int direction) {
		// IStructuredSelection sel = (IStructuredSelection)
		// getFavoritesTable().getSelection();
		// List<?> selList = sel.toList();
		// Object[] movedFavs= new Object[getFavorites().size()];
		// int i;
		// for (Iterator<?> favs = selList.iterator(); favs.hasNext();) {
		// Object config = favs.next();
		// i= getFavorites().indexOf(config);
		// movedFavs[i + direction]= config;
		// }
		//
		// getFavorites().removeAll(selList);
		//
		// for (int j = 0; j < movedFavs.length; j++) {
		// Object config = movedFavs[j];
		// if (config != null) {
		// getFavorites().add(j, (ILaunchConfiguration) config);
		// }
		// }
		// getFavoritesTable().refresh();
		// handleFavoriteSelectionChanged();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(350, 400);
	}

	protected TableViewer getFavoritesTable() {
		return favorites;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		getShell().setText(Messages.organize_favorites_dialog_header);
		createWorkingCopies();
		createFavoritesArea(composite);
		handleFavoriteSelectionChanged();
		return composite;
	}

	private void createWorkingCopies() {
		this.copies = Functional.map(
				Functions.toworkingcopy(),
				ExtendedLaunchConfigurationRepositoryProvider.INSTANCE.get().list());
	}

	// @Override
	// protected Control createContents(Composite parent) {
	// Control contents = super.createContents(parent);
	// PlatformUI.getWorkbench().getHelpSystem().setHelp(getDialogArea(),
	// IDebugHelpContextIds.ORGANIZE_FAVORITES_DIALOG);
	// return contents;
	// }

	/**
	 * Creates the main area of the dialog
	 *
	 * @param parent
	 *            the parent to add this content to
	 */
	private void createFavoritesArea(Composite parent) {
		Composite topComp = SWTFactory.createComposite(parent, parent.getFont(), 2, 1, GridData.FILL_BOTH, 0, 0);
		SWTFactory.createLabel(topComp, Messages.organize_favorites_dialog_label, 2);
		favorites = createTable(topComp, new FavoritesContentProvider());
		Composite buttons = SWTFactory.createComposite(topComp, topComp.getFont(), 1, 1,
				GridData.VERTICAL_ALIGN_BEGINNING, 0, 0);

		btnAddFavorite = SWTFactory.createPushButton(buttons, Messages.organize_favorites_dialog_btn_add, null);
		btnAddFavorite.addSelectionListener(buttonPressedListener);
		btnAddFavorite.setEnabled(true);

		btnRemoveFavorites = SWTFactory.createPushButton(buttons, Messages.organize_favorites_dialog_btn_remove, null);
		btnRemoveFavorites.addSelectionListener(buttonPressedListener);

		btnMoveUp = SWTFactory.createPushButton(buttons, Messages.organize_favorites_dialog_btn_up, null);
		btnMoveUp.addSelectionListener(buttonPressedListener);

		btnMoveDown = SWTFactory.createPushButton(buttons, Messages.organize_favorites_dialog_btn_down, null);
		btnMoveDown.addSelectionListener(buttonPressedListener);
	}

	/**
	 * Creates a fully configured table with the given content provider
	 */
	private TableViewer createTable(Composite parent, IContentProvider contentProvider) {
		TableViewer tableViewer = new TableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.setLabelProvider(new ExtendedLaunchConfigurationLabelProvider());
		tableViewer.setContentProvider(contentProvider);
		tableViewer.setInput("!!!!!!! one more ");
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 100;
		gd.heightHint = 100;
		tableViewer.getTable().setLayoutData(gd);
		tableViewer.getTable().setFont(parent.getFont());
		tableViewer.addSelectionChangedListener(selectionChangedListener);
		tableViewer.getControl().addKeyListener(keyPressedListener);
		return tableViewer;
	}

	/**
	 * Refresh all tables and buttons
	 */
	protected void updateStatus() {
		getFavoritesTable().refresh();
		handleFavoriteSelectionChanged();
	}

	/**
	 * The selection in the favorites list has changed
	 */
	protected void handleFavoriteSelectionChanged() {
		// IStructuredSelection selection = (IStructuredSelection)
		// getFavoritesTable().getSelection();
		// List<ILaunchConfiguration> favs = getFavorites();
		// boolean notEmpty = !selection.isEmpty();
		// Iterator<?> elements = selection.iterator();
		// boolean first= false;
		// boolean last= false;
		// int lastFav= favs.size() - 1;
		// while (elements.hasNext()) {
		// Object element = elements.next();
		// if(!first && favs.indexOf(element) == 0) {
		// first= true;
		// }
		// if (!last && favs.indexOf(element) == lastFav) {
		// last= true;
		// }
		// }
		//
		// btnRemoveFavorites.setEnabled(notEmpty);
		// btnMoveUp.setEnabled(notEmpty && !first);
		// btnMoveDown.setEnabled(notEmpty && !last);
	}

	@Override
	protected void okPressed() {
		saveFavorites();
		super.okPressed();
	}

//	@Override
//	protected IDialogSettings getDialogBoundsSettings() {
//		IDialogSettings settings = DebugUIPlugin.getDefault().getDialogSettings();
//		IDialogSettings section = settings.getSection(getDialogSettingsSectionName());
//		if (section == null) {
//			section = settings.addNewSection(getDialogSettingsSectionName());
//		}
//		return section;
//	}
//
//	/**
//	 * Returns the name of the section that this dialog stores its settings in
//	 *
//	 * @return String
//	 */
//	private String getDialogSettingsSectionName() {
//		return "FAVORITES_DIALOG_SECTION"; //$NON-NLS-1$
//	}

	/**
	 * Method performOK. Uses scheduled Job format.
	 *
	 * @since 3.2
	 */
	public void saveFavorites() {
		for (IExtendedLaunchConfigurationWorkingCopy copy : copies) {
			ExtendedLaunchConfigurationRepositoryProvider.INSTANCE.get().save(copy);
		}

		// TODO save
		// final Job job = new Job(Messages.FavoritesDialog_8) {
		// @SuppressWarnings("deprecation")
		// @Override
		// protected IStatus run(IProgressMonitor monitor) {
		// ILaunchConfiguration[] initial = getInitialFavorites();
		// List<ILaunchConfiguration> current = getFavorites();
		// String groupId = getLaunchHistory().getLaunchGroup().getIdentifier();
		//
		// int taskSize = Math.abs(initial.length-current.size());//get task
		// size
		// monitor.beginTask(Messages.FavoritesDialog_8, taskSize);//and set it
		//
		// // removed favorites
		// for (int i = 0; i < initial.length; i++) {
		// ILaunchConfiguration configuration = initial[i];
		// if (!current.contains(configuration)) {
		// // remove fav attributes
		// try {
		// ILaunchConfigurationWorkingCopy workingCopy =
		// configuration.getWorkingCopy();
		// workingCopy.setAttribute(IDebugUIConstants.ATTR_DEBUG_FAVORITE,
		// (String)null);
		// workingCopy.setAttribute(IDebugUIConstants.ATTR_DEBUG_FAVORITE,
		// (String)null);
		// List<String> groups =
		// workingCopy.getAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
		// (List<String>) null);
		// if (groups != null) {
		// groups.remove(groupId);
		// if (groups.isEmpty()) {
		// groups = null;
		// }
		// workingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
		// groups);
		// }
		// workingCopy.doSave();
		// } catch (CoreException e) {
		// DebugUIPlugin.log(e);
		// return Status.CANCEL_STATUS;
		// }
		// }
		// monitor.worked(1);
		// }
		//
		// // update added favorites
		// Iterator<ILaunchConfiguration> favs = current.iterator();
		// while (favs.hasNext()) {
		// ILaunchConfiguration configuration = favs.next();
		// try {
		// List<String> groups =
		// configuration.getAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
		// (List<String>) null);
		// if (groups == null) {
		// groups = new ArrayList<String>();
		// }
		// if (!groups.contains(groupId)) {
		// groups.add(groupId);
		// ILaunchConfigurationWorkingCopy workingCopy =
		// configuration.getWorkingCopy();
		// workingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
		// groups);
		// workingCopy.doSave();
		// }
		// } catch (CoreException e) {
		// DebugUIPlugin.log(e);
		// return Status.CANCEL_STATUS;
		// }
		// monitor.worked(1);
		// }
		//
		// fHistory.setFavorites(getArray(current));
		// monitor.done();
		// return Status.OK_STATUS;
		// }
		// };
		// job.setPriority(Job.LONG);
		// PlatformUI.getWorkbench().getProgressService().showInDialog(getParentShell(),
		// job);
		// job.schedule();

	}

}
