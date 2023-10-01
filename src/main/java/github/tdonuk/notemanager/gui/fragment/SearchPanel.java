package github.tdonuk.notemanager.gui.fragment;

import github.tdonuk.notemanager.constant.Application;
import github.tdonuk.notemanager.gui.component.Button;
import github.tdonuk.notemanager.gui.component.CustomTextField;
import github.tdonuk.notemanager.gui.container.Panel;
import github.tdonuk.notemanager.gui.util.SearchFieldDocumentListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class SearchPanel extends Panel {
	private final JTextField searchInput;
	
	public SearchPanel(Runnable doRemoveAllMarks, Runnable doSearch) {
		searchInput = new CustomTextField(20);
		
		searchInput.getDocument().addDocumentListener(new SearchFieldDocumentListener(doSearch));
		
		JButton next = new Button("Next");
		next.setEnabled(false);
		
		JButton previous = new Button("Previous");
		previous.setEnabled(false);
		
		JButton close = new Button("X");
		close.setContentAreaFilled(false);
		close.addActionListener(a -> {
			this.setVisible(false);
			doRemoveAllMarks.run();
		});
		
		this.add(searchInput);
		this.add(next);
		this.add(previous);
		this.add(close);
		
		this.setFont(Application.PRIMARY_FONT);
		this.setToolTipText("Press ESC to close");
		
		SearchPanel searchPanel = this;
		
		this.registerKeyboardAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchPanel.setVisible(false);
				doRemoveAllMarks.run();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		searchInput.requestFocus();
		searchInput.selectAll();
	}
	
	public JTextField getSearchInput() {
		return searchInput;
	}
}
