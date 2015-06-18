/*******************************************************************************
 * Copyright (c) 2015 Willy du Preez
 * Copyright (c) 2007, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Willy du Preez - copied and changed to work for all launch configurations
 *******************************************************************************/
package com.rawpixil.eclipse.launchpad.internal.ui.component.dialog;

import java.util.List;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationWorkingCopy;
import com.rawpixil.eclipse.launchpad.internal.core.extended.filter.Predicates;
import com.rawpixil.eclipse.launchpad.internal.message.Messages;
import com.rawpixil.eclipse.launchpad.internal.ui.presentation.LaunchPadModelPresentationProvider;
import com.rawpixil.eclipse.launchpad.internal.util.Assert;
import com.rawpixil.eclipse.launchpad.internal.util.Log;
import com.rawpixil.eclipse.launchpad.internal.util.functional.Functional;

/**
 * This dialog is used to select one or more launch configurations to add to your favorites
 */
public class SelectFavoritesDialog extends AbstractCheckboxSelectionDialog {

	private List<IExtendedLaunchConfigurationWorkingCopy> copies;

	/**
	 * Content provider for table
	 */
	protected class LaunchConfigurationContentProvider implements IStructuredContentProvider {
		@Override
		public Object[] getElements(Object inputElement) {
			Log.log("LaunchConfigurationContentProvider expecting !!!!!!: " + inputElement);
			return Functional.filter(copies, Predicates.favorites().negate()).toArray();
//			ILaunchConfiguration[] all = null;
//			try {
//				all = LaunchConfigurationManager.filterConfigs(DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations());
//			} catch (CoreException e) {
//				DebugUIPlugin.log(e);
//				return new ILaunchConfiguration[0];
//			}
//			List<ILaunchConfiguration> list = new ArrayList<ILaunchConfiguration>(all.length);
//			// TODO Filter
////			ViewerFilter filter = new LaunchGroupFilter(fHistory.getLaunchGroup());
////			for (int i = 0; i < all.length; i++) {
////				if (filter.select(null, null, all[i])) {
////					list.add(all[i]);
////				}
////			}
//			list.removeAll(fCurrentFavoriteSet);
//			Object[] objs = list.toArray();
//			new WorkbenchViewerComparator().sort(getCheckBoxTableViewer(), objs);
//			return objs;
		}

		@Override
		public void dispose() {}
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
	}

	public SelectFavoritesDialog(Shell parentShell, List<IExtendedLaunchConfigurationWorkingCopy> copies) {
		super(parentShell);
		Assert.notNull(copies, "Working copies cannot be null");
		setTitle(Messages.organize_favorites_select_dialog_add_btn);
		setShowSelectAllButtons(true);
		this.copies = copies;
	}

//	@Override
//	protected String getDialogSettingsId() {
//		return IDebugUIConstants.PLUGIN_ID + ".SELECT_FAVORITESS_DIALOG"; //$NON-NLS-1$
//	}

	@Override
	protected Object getViewerInput() {
		return "!!!!!!!!";
//		return fHistory.getLaunchGroup().getMode();
	}

	@Override
	protected IBaseLabelProvider getLabelProvider() {
		return new FavoritesLabelProvider(LaunchPadModelPresentationProvider.INSTANCE.get());
	}

	@Override
	protected IContentProvider getContentProvider() {
		return new LaunchConfigurationContentProvider();
	}

	@Override
	protected String getHelpContextId() {
		return null;//IDebugHelpContextIds.SELECT_FAVORITES_DIALOG;
	}

	@Override
	protected String getViewerLabel() {
		return null;//LaunchConfigurationsMessages.FavoritesDialog_7;
	}

}
