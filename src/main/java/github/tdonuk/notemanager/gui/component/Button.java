package github.tdonuk.notemanager.gui.component;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Button extends JButton {
	public Button(String title) {
		super(title);
	}
	
	public Button(String title, ActionListener listener) {
		super(title);
		
		this.addActionListener(listener);
	}
}
