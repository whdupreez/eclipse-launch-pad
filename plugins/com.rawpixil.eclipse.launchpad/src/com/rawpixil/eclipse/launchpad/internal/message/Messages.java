package com.rawpixil.eclipse.launchpad.internal.message;

import org.eclipse.osgi.util.NLS;

public class Messages {

	private static final String BUNDLE_NAME = Messages.class.getPackage().getName() + ".messages"; //$NON-NLS-1$

	public static String delete_launch_configuration;
	public static String delete_launch_configuration_dialog_header;
	public static String delete_launch_configuration_dialog_description;

	public static String delete_launch_configurations_dialog_header;
	public static String delete_launch_configurations_dialog_description;

	public static String rename_launch_configuration;
	public static String rename_launch_configuration_dialog_header;
	public static String rename_launch_configuration_dialog_description;
	public static String rename_launch_configuration_error_empty;
	public static String rename_launch_configuration_error_exists;
	public static String rename_launch_configuration_error_invalid;

	/* -------------------------------------------------------------
	 *  Organize Favorites
	 * ------------------------------------------------------------- */

	public static String organize_favorites_action;

	public static String organize_favorites_dialog_header;
	public static String organize_favorites_dialog_label;
	public static String organize_favorites_dialog_btn_add;
	public static String organize_favorites_dialog_btn_remove;
	public static String organize_favorites_dialog_btn_up;
	public static String organize_favorites_dialog_btn_down;

	public static String organize_favorites_select_dialog_header;
	public static String organize_favorites_select_dialog_label;
	public static String organize_favorites_select_dialog_add_btn;
	public static String organize_favorites_save_message;

	/* -------------------------------------------------------------
	 *  Extended launcher
	 * ------------------------------------------------------------- */

	public static String extended_launcher_error_cannot_launch_title;
	public static String extended_launcher_error_cannot_launch_description;

	/* -------------------------------------------------------------
	 *  Terminate launch
	 * ------------------------------------------------------------- */

	public static String terminate_launch_action;
	public static String terminate_launch_dialog_error_title;
	public static String terminate_launch_dialog_error_description;

	public static String terminate_and_relaunch_action;

	/* -------------------------------------------------------------
	 *  Common
	 * ------------------------------------------------------------- */

	public static String checkbox_selection_dialog_select_all_btn;
	public static String checkbox_selection_dialog_deselect_all_btn;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
