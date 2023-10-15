package github.tdonuk.notemanager.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DialogUtils {
	public static JFileChooser fileChooser(boolean forSave) {
		JFileChooser fileChooser = new JFileChooser(EnvironmentUtils.userDir()){
			@Override
			public void approveSelection(){
				File f = getSelectedFile();
				if(f.exists() && forSave){
					int result = JOptionPane.showConfirmDialog(this,"The file already exists. Overwrite?","Existing file",JOptionPane.YES_NO_OPTION);
					switch(result) {
						case JOptionPane.YES_OPTION -> {
							super.approveSelection();
							return;
						}
						case JOptionPane.NO_OPTION, JOptionPane.CLOSED_OPTION -> {
							return;
						}
						case JOptionPane.CANCEL_OPTION -> {
							cancelSelection();
							return;
						}
						default -> cancelSelection();
					}
				}
				super.approveSelection();
			}
		};
		fileChooser.setDialogTitle("Select file a to open");
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDragEnabled(true);
		if(forSave) fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		
		return fileChooser;
	}
	
	public static File askFileForOpen() {
		JFileChooser fileChooser = fileChooser(false);
		
		int result = fileChooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} else return null;
	}
	
	public static File askFileForSave(File initial) {
		JFileChooser fileChooser = fileChooser(true);
		fileChooser.setSelectedFile(initial);
		
		int result = fileChooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} else return null;
	}
	
	/**
	 *
	 * @param message Message to show the user
	 * @param title Title of the dialog
	 * @return true, if selected 'Yes' option, false otherwise
	 */
	public static Boolean askApprovation(String message, String title) {
		EnvironmentUtils.beep();
		return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null) == 0; // Yes: 0, No: 1
	}
	
	public static void showError(String message, String title) {
		EnvironmentUtils.beep();
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
