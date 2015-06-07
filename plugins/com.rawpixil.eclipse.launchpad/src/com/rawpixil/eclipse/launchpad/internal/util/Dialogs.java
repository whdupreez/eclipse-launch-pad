package com.rawpixil.eclipse.launchpad.internal.util;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

public final class Dialogs {

	public static boolean confirm(String title, String message) {
		return MessageDialog.openConfirm(Displays.getActiveShell(), title, message);
	}

	public static void error(String title, String message) {
		MessageDialog.openError(Displays.getActiveShell(), title, message);
	}

	public static void info(String title, String message) {
		MessageDialog.openInformation(Displays.getActiveShell(), title, message);
	}

	public static Optional<String> input(String title, String message, String initialValue, IInputValidator validator) {
		InputDialog dialog = new InputDialog(Displays.getActiveShell(), title, message, initialValue, validator);
		dialog.setBlockOnOpen(true);
		dialog.open();
		if (dialog.getReturnCode() == Window.OK) {
			return Optional.ofNullable(dialog.getValue());
		}
		else {
			return Optional.empty();
		}
	}

//	public static void favourites() {
//		FavoritesDialog dialog = new FavoritesDialog(Displays.getActiveShell(), history);
//	}

	private Dialogs() {
	}

}
