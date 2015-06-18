package com.rawpixil.eclipse.launchpad.internal.ui;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public interface IPresentation<T> {

	Image getImage(T model);

	String getText(T model);
	StyledString getStyledText(T model);

}
