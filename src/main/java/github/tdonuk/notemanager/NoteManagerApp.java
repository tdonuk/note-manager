package github.tdonuk.notemanager;

import github.tdonuk.notemanager.gui.window.MainWindow;
import github.tdonuk.notemanager.gui.constant.EditorState;
import github.tdonuk.notemanager.util.DialogUtils;
import github.tdonuk.notemanager.util.EnvironmentUtils;
import github.tdonuk.notemanager.util.StateDTO;
import github.tdonuk.notemanager.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;

@Slf4j
public class NoteManagerApp {
	public static void main(String[] args) {
		try {
			initUIStyle();
			
			initMainWindow();
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

	private static void initMainWindow() {
		MainWindow mainWindow = MainWindow.getInstance();
		mainWindow.setVisible(true);

		// state management
		try {
			log.info("looking for persisted state..");
			File persistedStateFile = new File(EnvironmentUtils.stateFileDir());

			if(persistedStateFile.canRead()) {
				log.info("found state file. app will be initialized with state.");
				mainWindow.initializeWithState((StateDTO) StringUtils.parseJSON(Files.readString(persistedStateFile.toPath()), StateDTO.class));
			} else {
				log.info("cannot found any state file. app will start with fresh state");
				mainWindow.initializeWithoutState();
			}
		} catch (Exception e) {
			log.error("cannot initialize with session. app will start with fresh state.");
			mainWindow.initializeWithoutState();
		}

		MainWindow.updateState(EditorState.READY);
	}
}
