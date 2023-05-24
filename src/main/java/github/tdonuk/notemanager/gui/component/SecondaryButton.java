package github.tdonuk.notemanager.gui.component;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SecondaryButton extends JButton {
	public SecondaryButton(String title) {
		super(title);
		init();
	}
	
	public SecondaryButton(String title, ActionListener listener) {
		super(title);
		init();
		
		this.addActionListener(listener);
	}
	
	private void init() {

	}
}
