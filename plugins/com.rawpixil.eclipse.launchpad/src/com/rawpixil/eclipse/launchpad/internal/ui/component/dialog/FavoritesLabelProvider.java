package com.rawpixil.eclipse.launchpad.internal.ui.component.dialog;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.rawpixil.eclipse.launchpad.internal.ui.IPresentation;

public class FavoritesLabelProvider extends LabelProvider {

	private IPresentation<Object> presentation;

	public FavoritesLabelProvider(IPresentation<Object> presentation) {
		this.presentation = presentation;
	}

	@Override
	public Image getImage(Object element) {
		return this.presentation.getImage(element);
	}

	@Override
	public String getText(Object element) {
		return this.presentation.getText(element);
	}

}
