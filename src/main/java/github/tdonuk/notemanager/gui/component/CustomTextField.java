package github.tdonuk.notemanager.gui.component;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CustomTextField extends JTextField {
	public CustomTextField(int maxLength) {
		super(maxLength);
		
		JTextField field = this;
		
		this.addKeyListener(new KeyListener() { // this is for length limit
			@Override
			public void keyTyped(KeyEvent e) {
				if(field.getText().length() > maxLength) e.consume();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			
			}
		});
	}
}
