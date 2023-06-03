package github.tdonuk.notemanager.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DialogUtils {
	public static File askForOpen() {
		JFileChooser fileChooser = fileChooser(false);
		
		int result = fileChooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} else return null;
	}
	
	public static JFileChooser fileChooser(boolean saveFlag) {
		JFileChooser fileChooser = new JFileChooser(EnvironmentUtils.userDir());
		fileChooser.setDialogTitle("Select file a to open");
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(saveFlag) fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

		return fileChooser;
	}
	
	public static File askForSave(File initial) {
		JFileChooser fileChooser = fileChooser(true);
		fileChooser.setSelectedFile(initial);
		
		int result = fileChooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} else return null;
	}
	
	
}
