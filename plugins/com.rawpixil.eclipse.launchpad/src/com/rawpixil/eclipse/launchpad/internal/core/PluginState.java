package com.rawpixil.eclipse.launchpad.internal.core;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.rawpixil.eclipse.launchpad.LaunchPadPlugin;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationRepository;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationWorkingCopy;
import com.rawpixil.eclipse.launchpad.core.IPluginState;
import com.rawpixil.eclipse.launchpad.internal.core.extended.ExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.core.extended.ExtendedLaunchConfigurationRepositoryProvider;
import com.rawpixil.eclipse.launchpad.internal.util.ELF;
import com.rawpixil.eclipse.launchpad.internal.util.XML;

public class PluginState implements IPluginState {

	private static final String PLUGIN_STATE_FILE = LaunchPadPlugin.PLUGIN_ID + "-state";

	private static final String ROOT_ELEMENT_NAME = "launchpad";
	private static final String ROOT_ELEMENT_VERSION_ATTR = "version";
	private static final String ROOT_ELEMENT_VERSION_VALUE = "1.0.0";

	private static final String REPOSITORY_ELEMENT_NAME = "extended-launch-configurations";
	private static final String REPOSITORY_ENTRY_ELEMENT_NAME = "extended-launch-configuration";

	private static final String CONFIG_ATTR = "config";
	private static final String FAVORITE_ATTR = "favorite";

	private File state(int version) {
		String state = PLUGIN_STATE_FILE + "-" + version + ".xml";
		return LaunchPadPlugin.getDefault().getStateLocation().append(state).toFile();
	}

	@Override
	public void prepareToSave(ISaveContext context) throws CoreException {
	}

	@Override
	public void saving(ISaveContext context) throws CoreException {
		File state = this.state(context.getSaveNumber());
		Document document = toDocument();
		XML.write(document, state);
		context.map(new Path(PLUGIN_STATE_FILE), new Path(state.getName()));
		context.needSaveNumber();
	}

	private Document toDocument() throws CoreException {
		Document document = XML.newDocument();
		Element root = root(document);
		document.appendChild(root);

		Element repository = repository(document);
		root.appendChild(repository);

		return document;
	}

	private Element root(Document document) {
		Element root = document.createElement(ROOT_ELEMENT_NAME);
		root.setAttribute(ROOT_ELEMENT_VERSION_ATTR, ROOT_ELEMENT_VERSION_VALUE);
		return root;
	}

	private Element repository(Document document) {
		Element repository = document.createElement(REPOSITORY_ELEMENT_NAME);

		IExtendedLaunchConfigurationRepository repo = ExtendedLaunchConfigurationRepositoryProvider.INSTANCE.get();
		List<IExtendedLaunchConfiguration> items = repo.list();
		for (IExtendedLaunchConfiguration item : items) {
			Element entry = document.createElement(REPOSITORY_ENTRY_ELEMENT_NAME);
			entry.setAttribute(FAVORITE_ATTR, Boolean.toString(item.isFavorite()));
			entry.setAttribute(CONFIG_ATTR,item.getLaunchConfiguration().getName());
			repository.appendChild(entry);
		}

		return repository;
	}


	@Override
	public void doneSaving(ISaveContext context) {
		this.state(context.getPreviousSaveNumber()).delete();
	}

	@Override
	public void rollback(ISaveContext context) {
	}

	@Override
	public void restore(ISavedState saved) throws CoreException {
		// First restore the default so that all load
		// configurations in the workspace are loaded,
		// and not only those contained in the saved
		// state.
		this.restoreDefault();
		if (saved != null) {
			// Update the configurations for which a saved
			// state exist.
			updateFromFile(saved);
		}
	}

	private void restoreDefault() throws CoreException {
		IExtendedLaunchConfigurationRepository repo = ExtendedLaunchConfigurationRepositoryProvider.INSTANCE.get();

		for (ILaunchConfiguration config : ELF.getLaunchConfigurations()) {
			IExtendedLaunchConfiguration extended = new ExtendedLaunchConfiguration(config);
			repo.add(extended);
		}
	}

	private void updateFromFile(ISavedState saved) throws CoreException {
		try {
			IPath location = saved.lookup(new Path(PLUGIN_STATE_FILE));
			if (location != null) {
				File file = LaunchPadPlugin.getDefault().getStateLocation().append(location).toFile();
				updateFromFile(file);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void updateFromFile(File file) throws CoreException {
		IExtendedLaunchConfigurationRepository repo = ExtendedLaunchConfigurationRepositoryProvider.INSTANCE.get();
		List<IExtendedLaunchConfiguration> config = repo.list();
		Document document = XML.read(file);
		NodeList nodes = document.getElementsByTagName(REPOSITORY_ENTRY_ELEMENT_NAME);
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			Element element = (Element) nodes.item(idx);
			String name = element.getAttribute(CONFIG_ATTR);
			boolean favorite = Boolean.parseBoolean(element.getAttribute(FAVORITE_ATTR));
			for (IExtendedLaunchConfiguration item : config) {
				if (item.getLaunchConfiguration().getName().equals(name)) {
					IExtendedLaunchConfigurationWorkingCopy copy = item.getWorkingCopy();
					copy.setFavorite(favorite);
					repo.save(copy);
				}
			}
		}
	}

}
