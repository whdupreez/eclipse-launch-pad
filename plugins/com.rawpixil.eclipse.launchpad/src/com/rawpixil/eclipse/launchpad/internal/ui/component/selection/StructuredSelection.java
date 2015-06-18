package com.rawpixil.eclipse.launchpad.internal.ui.component.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.rawpixil.eclipse.launchpad.internal.util.Assert;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public class StructuredSelection {

	private IStructuredSelection selection;
	private List<?> items;

	public StructuredSelection(IStructuredSelection selection) {
		Assert.notNull(selection, "Selection cannot be null");
		this.selection = selection;
		this.items = this.selection.toList();
	}

	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	public boolean containsSingleItem() {
		return this.items.size() == 1;
	}

	public boolean containsSingleItemOfType() {
		return this.containsSingleItem() ? containsSingleItemOfType(this.items.get(0).getClass()) : false;
	}

	public boolean containsSingleItemOfType(Class<?> type) {
		Assert.notNull(type, "Type cannot be null");
		if (this.containsSingleItem()) {
			return type.isAssignableFrom(this.items.get(0).getClass());
		}
		return false;
	}

	public <T> Optional<T> getSingleItemOfType(Class<T> type) {
		Assert.notNull(type, "Type cannot be null");
		if (this.containsSingleItemOfType(type)) {
			return Optional.of(type.cast(this.items.get(0)));
		}
		else {
			return Optional.empty();
		}
	}

	public boolean containsMultipleItems() {
		return this.items.size() > 1;
	}

	public boolean containsMultipleItemsOfType(Class<?> type) {
		Assert.notNull(type, "Type cannot be null");
		if (!this.containsMultipleItems()) {
			return false;
		}
		for (Object item : this.items) {
			if (!type.isAssignableFrom(item.getClass())) {
				return false;
			}
		}
		return true;
	}

	public boolean containsOnlyItemsOfType(Class<?> type) {
		Assert.notNull(type, "Type cannot be null");
		if (this.isEmpty()) {
			return false;
		}
		for (Object item : this.items) {
			if (!type.isAssignableFrom(item.getClass())) {
				return false;
			}
		}
		return true;
	}

	public <T> Optional<List<T>> getMultipleItemsOfType(Class<T> type) {
		Assert.notNull(type, "Type cannot be null");
		if (this.containsMultipleItemsOfType(type)) {
			@SuppressWarnings("unchecked")
			Optional<List<T>> selected = Optional.of((List<T>) selectedItems());
			return selected;
		}
		return Optional.empty();
	}

	public <T> Optional<List<T>> getOnlyItemsOfType(Class<T> type) {
		Assert.notNull(type, "Type cannot be null");
		if (this.containsOnlyItemsOfType(type)) {
			@SuppressWarnings("unchecked")
			Optional<List<T>> selected = Optional.of((List<T>) selectedItems());
			return selected;
		}
		return Optional.empty();
	}

	public <T> List<T> getItemsOfType(Class<T> type) {
		Assert.notNull(type, "Type cannot be null");
		List<T> matched = new ArrayList<T>();
		if (!this.isEmpty()) {
			for (Object item : this.items) {
				if (type.isAssignableFrom(item.getClass())) {
					matched.add(type.cast(item));
				}
			}
		}
		return matched;
	}

	public List<?> selectedItems() {
		return this.items;
	}

}
