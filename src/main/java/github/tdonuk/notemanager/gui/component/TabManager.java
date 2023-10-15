package github.tdonuk.notemanager.gui.component;

import github.tdonuk.notemanager.gui.event.listener.TabListener;

import java.io.IOException;

public interface TabManager <Tab, TabObject> {
	Tab addTab(TabObject obj, boolean isTemporary) throws IOException;
	
	Tab getTab(TabObject obj);
	
	void setSelectedTab(Tab tab);
	
	void addTabListener(TabListener tabListener);
	
	void removeTabListener(TabListener tabListener);
	
	void clearTabListeners();
}
