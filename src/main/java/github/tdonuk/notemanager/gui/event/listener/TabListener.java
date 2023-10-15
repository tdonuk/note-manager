package github.tdonuk.notemanager.gui.event.listener;

import github.tdonuk.notemanager.gui.event.domain.TabEvent;

public interface TabListener extends CustomEventListener {
	void tabAdded(TabEvent e);
	void tabRemoved(TabEvent e);
}
