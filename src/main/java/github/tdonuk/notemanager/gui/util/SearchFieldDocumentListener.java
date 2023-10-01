package github.tdonuk.notemanager.gui.util;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchFieldDocumentListener implements DocumentListener {
	
	private final Runnable doSearch;
	
	public SearchFieldDocumentListener(Runnable doSearch) {
		this.doSearch = doSearch;
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		doSearch.run();
	}
	
	@Override
	public void removeUpdate(DocumentEvent e) {
		doSearch.run();
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		// Leave this blank
	}
}
