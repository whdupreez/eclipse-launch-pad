package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import org.eclipse.jface.action.Action;

import com.rawpixil.eclipse.launchpad.internal.message.Messages;
import com.rawpixil.eclipse.launchpad.internal.ui.component.dialog.OrganizeFavoritesDialog;
import com.rawpixil.eclipse.launchpad.internal.util.Displays;

public class OrganizeFavoritesAction extends Action {

	public OrganizeFavoritesAction() {
		super(Messages.organize_favorites_action, null);
	}

	@Override
	public void run() {
		OrganizeFavoritesDialog dialog = new OrganizeFavoritesDialog(Displays.getActiveShell());
		dialog.setBlockOnOpen(true);
		dialog.open();
	}

}
