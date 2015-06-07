package com.rawpixil.eclipse.launchpad.core;

import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.runtime.CoreException;

public interface IPluginState extends ISaveParticipant {

	void restore(ISavedState saved) throws CoreException;

}
