package github.tdonuk.notemanager.gui.dialog;

import github.tdonuk.notemanager.gui.container.Panel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UndecoratedDialog extends JDialog {
	protected Panel contentPane;
	public UndecoratedDialog(Point location) {
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		
		if(location != null) setLocation(location);
		else setLocationRelativeTo(null);
		
		contentPane = new Panel();
		contentPane.setBorder(new LineBorder(Color.black, 1));
		
		setContentPane(contentPane);
	}
}
