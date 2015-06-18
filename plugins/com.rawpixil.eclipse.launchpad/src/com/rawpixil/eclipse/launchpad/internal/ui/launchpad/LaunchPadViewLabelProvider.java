package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.ui.IPresentation;

public class LaunchPadViewLabelProvider extends StyledCellLabelProvider implements ILabelProvider {

	private IPresentation<Object> presentation;

	public LaunchPadViewLabelProvider(IPresentation<Object> presentation) {
		this.presentation = presentation;
	}

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		StyledString styled = this.presentation.getStyledText(element);
		cell.setText(styled.getString());
		cell.setStyleRanges(styled.getStyleRanges());
		cell.setImage(this.presentation.getImage(element));
		super.update(cell);
	}

	@Override
	public Image getImage(Object element) {
		// Only provide images for extended launch configurations
		// This is used only by the tree viewer for filtering
		// on the filtered tree implementation.
		if (element instanceof IExtendedLaunchConfiguration) {
			return this.presentation.getImage(element);
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		// Only provide text for extended launch configurations
		// This is used only by the tree viewer for filtering
		// on the filtered tree implementation.
		if (element instanceof IExtendedLaunchConfiguration) {
			return this.presentation.getText(element);
		}
		return null;
	}

	@Override
	public void dispose() {
		super.dispose();
		this.presentation = null;
	}

}
