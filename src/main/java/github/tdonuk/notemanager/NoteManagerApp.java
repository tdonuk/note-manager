package github.tdonuk.notemanager;

import github.tdonuk.notemanager.gui.MainWindow;
import github.tdonuk.notemanager.gui.constant.EditorState;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;

@Slf4j
public class NoteManagerApp {
	public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			log.info("found Look & Feel: "+info.getName()); // to see all look and feel alternatives
			if ("Windows".equals(info.getName()) || "GTK+".equals(info.getName())) { // Gui style
				log.info(info.getName() + " will be selected as system default");
				UIManager.setLookAndFeel(info.getClassName());
			}
		}
		MainWindow mainWindow = MainWindow.getInstance();
		mainWindow.setVisible(true);
		
		MainWindow.updateState(EditorState.READY);
	}
}
