package github.tdonuk.notemanager.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DialogUtils {
	public static File askForFile() {
		JFileChooser fileChooser = new JFileChooser(EnvironmentUtils.workingDir());
		fileChooser.setDialogTitle("Select file a to open");
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result = fileChooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		else return null;
	}
}
