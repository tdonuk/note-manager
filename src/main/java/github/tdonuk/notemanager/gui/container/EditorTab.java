package github.tdonuk.notemanager.gui.container;

import github.tdonuk.notemanager.constant.FileType;
import github.tdonuk.notemanager.exception.CustomException;
import github.tdonuk.notemanager.gui.component.Editor;
import github.tdonuk.notemanager.gui.component.EditorTabManager;
import github.tdonuk.notemanager.util.DialogUtils;
import github.tdonuk.notemanager.util.EnvironmentUtils;
import github.tdonuk.notemanager.util.StringUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Represents the tabs of the tabbed pane
 */
@Getter
@Setter
@Slf4j
public class EditorTab extends JPanel {
	private EditorContainer editorContainer;

	private String title;
	private FileType type;
	private File openedFile;
	private boolean tempFlag = false; // to determine whether openedFile is actually exists and user has opened it to edit, or this tab has created as 'New Tab' with empty content that has no real file.
	
	public EditorTab(@NonNull File file, String title) throws IOException {
		super(new BorderLayout());
		
		this.title = title;
		this.openedFile = file;
		
		if(!openedFile.canRead()) tempFlag = true;

		init();
		
		reload(true);

		if(file.getName().contains(".")) {
			type = FileType.findByExtension(file.getName().substring(file.getName().lastIndexOf(".")));
		} else type = FileType.TXT;
		
		determineSyntaxHighlighting();
	}
	
	private void determineSyntaxHighlighting() {
		Editor editor = editorContainer.getEditorPane().getEditor();

		switch(type) {
			case XML, XSL, XSLT -> editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
			case HTML -> editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
			case JSON -> editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
			default -> editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		}
	}

	private void init() {
		this.editorContainer = new EditorContainer();
		this.add(editorContainer);
	}
	
	public File save() throws IOException {
		log.info("saving tab: " + openedFile.getAbsolutePath());
		if(openedFile.canWrite()) {
			Files.write(openedFile.toPath(), editorContainer.getEditorPane().getEditor().getText().getBytes());
			
			return openedFile;
		} else {
			File newFile = DialogUtils.askFileForSave(openedFile);
			
			if(newFile != null) {
				if(!newFile.canWrite() && !newFile.createNewFile()) throw new CustomException("couldn't able to create file to save");
				Files.write(newFile.toPath(), editorContainer.getEditorPane().getEditor().getText().getBytes());
				
				openedFile = newFile;
				
				return openedFile;
			}
			else return null;
		}
	}
	
	public void format() {
		Editor editor = editorContainer.getEditorPane().getEditor();
		
		log.info("formatting " + type.name().toLowerCase());
		
		if (editor.getText() == null || editor.getText().isBlank()) return;
		
		try {
			if (FileType.XML.equals(type)) editor.setText(StringUtils.formatXml(editor.getText()));
			else if(FileType.JSON.equals(type)) editor.setText(StringUtils.formatJson(editor.getText()));
		} catch (Exception ex) {
			throw new CustomException(ex);
		}
	}
	
	public void reload(boolean refreshContent) throws IOException {
		if(openedFile.canRead()) {
			if(refreshContent) editorContainer.getEditorPane().getEditor().setText(Files.readString(openedFile.toPath()));
		} else {
			if(!tempFlag) { // the tab is not temporary but still cant read file. this means the file is removed or renamed by another software.
				String message = "The file has deleted or not able to read content. Do you want to keep this tab?";
				EnvironmentUtils.beep();
				int response = JOptionPane.showConfirmDialog(null, message, "Can not read content", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				// 0: yes, 1: no
				
				if(response == 1) {
					removeSelf();
				} else tempFlag = true;
			}
		}
	}
	
	private void removeSelf() {
		EditorTabManager parent = (EditorTabManager) this.getParent();
		parent.remove(parent.indexOfComponent(this));
	}
	
	// title component of the tab
	public JPanel getHeader() {
		JPanel headerPanel = new Panel(new BorderLayout(5, 5));
		headerPanel.add(new JLabel(title, FileSystemView.getFileSystemView().getSystemIcon(openedFile), SwingConstants.LEADING),BorderLayout.WEST);
		
		JButton closeButton = new JButton("X");
		closeButton.addActionListener(a -> removeSelf());
		
		headerPanel.setOpaque(false);
		headerPanel.setBackground(null);
		
		closeButton.setBorder(null);
		closeButton.setContentAreaFilled(false);
		
		headerPanel.add(closeButton, BorderLayout.EAST);
		
		return headerPanel;
	}
	
	public boolean hasDiff() {
		try {
			if(tempFlag && getEditorContainer().getEditorPane().getEditor().getText().isBlank()) return false; // this tab is temp, but content is empty
			else if(!tempFlag && openedFile.canRead()) { // tab is not temp
				return ! Files.readString(openedFile.toPath()).equals(editorContainer.getEditorPane().getEditor().getText());
			} else return true; // tab is temp and has content in it
		} catch(Exception e) {
			DialogUtils.showError("Cannot check for diff: " + e.getMessage(), "Diff Check Failed");
			return true;
		}
	}
	
}
