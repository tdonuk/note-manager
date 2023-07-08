package github.tdonuk.notemanager.gui.dialog;

import javax.swing.*;

public abstract class AbstractDialog extends JDialog {
	protected AbstractDialog(JFrame owner, boolean modal) {
		super(owner, modal);
	}
	
	protected AbstractDialog(JFrame owner, String title, boolean modal) {
		super(owner, title, modal);
	}
}
