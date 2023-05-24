import gui.MainWindow;

import javax.swing.*;

public class NoteManagerApp {
	public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		MainWindow.getInstance().setVisible(true);
	}
}
