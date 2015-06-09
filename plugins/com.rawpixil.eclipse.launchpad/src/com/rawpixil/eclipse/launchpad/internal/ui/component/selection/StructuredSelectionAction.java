package com.rawpixil.eclipse.launchpad.internal.ui.component.selection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import com.rawpixil.eclipse.launchpad.internal.util.Optional;

// Generalize and utilize
public abstract class StructuredSelectionAction extends Action implements ISelectionChangedListener {

	private Optional<StructuredSelection> selection = Optional.empty();

	@Override
	public final void run() {
		if (this.selection.isPresent()) {
			this.handleSelection(selection.get());
		}
	}

	public final void refresh() {
		this.handleSelectionChanged(this.selection);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (event.getSelection() instanceof IStructuredSelection) {
			this.selection = Optional.of(new StructuredSelection((IStructuredSelection) event.getSelection()));
		}
		else {
			this.selection = Optional.empty();
		}
		this.handleSelectionChanged(this.selection);
	}

	protected abstract void handleSelection(StructuredSelection selection);
	// Indicates that a selection has changed, but it might not have been a structured selection?
	protected abstract void handleSelectionChanged(Optional<StructuredSelection> selection);

}
