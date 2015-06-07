package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.debug.ui.ILaunchGroup;

import com.rawpixil.eclipse.launchpad.internal.ui.component.selection.StructuredSelectionAction;
import com.rawpixil.eclipse.launchpad.internal.util.ELF;
import com.rawpixil.eclipse.launchpad.internal.util.comparator.Comparators;

public class LaunchActionFactory {

	// TODO move
	public static List<StructuredSelectionAction> actions() {
		List<StructuredSelectionAction> actions = new ArrayList<StructuredSelectionAction>();
		List<ILaunchGroup> groups = ELF.getLaunchGroups();
		Collections.sort(groups, Comparators.launchGroupCategoryComparator());
		for (ILaunchGroup group : groups) {
			if (group.isPublic()) {
				actions.add(new LaunchAction(group));
			}
		}
		return actions;
	}

}
