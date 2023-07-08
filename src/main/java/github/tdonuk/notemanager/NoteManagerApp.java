package github.tdonuk.notemanager;

import github.tdonuk.notemanager.gui.window.MainWindow;
import github.tdonuk.notemanager.gui.constant.EditorState;
import github.tdonuk.notemanager.util.DialogUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;

@Slf4j
public class NoteManagerApp {
	public static void main(String[] args) {
		try {
			initUIStyle();
			
			MainWindow mainWindow = MainWindow.getInstance();
			mainWindow.setVisible(true);
			
			MainWindow.updateState(EditorState.READY);
		} catch(Exception e) {
			DialogUtils.showError("Error", "Unexpected state: " + e.getMessage());
		}
	}
	
	private static void initUIStyle() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			log.info("found Look & Feel: "+info.getName()); // to see all look and feel alternatives
			if ("Windows".equals(info.getName()) || "GTK+".equals(info.getName())) { // Windows or Gnome (Tested on ubuntu)
				log.info(info.getName() + " will be selected as system default");
				UIManager.setLookAndFeel(info.getClassName());
			}
		}
	}
}
