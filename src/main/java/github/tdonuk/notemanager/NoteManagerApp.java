package github.tdonuk.notemanager;

import github.tdonuk.notemanager.gui.MainWindow;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class NoteManagerApp {
	public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			// System.out.println(info.getName()); // to see all look and feel alternatives
			if ("Windows Classic".equals(info.getName())) { // Gui style
				UIManager.setLookAndFeel(info.getClassName());
				break;
			}
		}
		MainWindow.getInstance().setVisible(true);
	}
}
