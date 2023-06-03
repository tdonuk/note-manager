package github.tdonuk.notemanager;

import github.tdonuk.notemanager.gui.MainWindow;
import github.tdonuk.notemanager.gui.constant.EditorState;

import javax.swing.*;
import java.io.IOException;

public class NoteManagerApp {
	public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			//log.info(info.getName()); // to see all look and feel alternatives
			if ("Windows Classic".equals(info.getName())) { // Gui style
				UIManager.setLookAndFeel(info.getClassName());
				break;
			}
		}
		MainWindow mainWindow = MainWindow.getInstance();
		mainWindow.setVisible(true);
		
		MainWindow.updateState(EditorState.READY);
	}
}
