package github.tdonuk.notemanager.gui.container;

import github.tdonuk.notemanager.constant.FileType;
import github.tdonuk.notemanager.domain.FileHist;
import github.tdonuk.notemanager.exception.CustomException;
import github.tdonuk.notemanager.gui.component.Editor;
import github.tdonuk.notemanager.gui.component.EditorTabManager;
import github.tdonuk.notemanager.util.DialogUtils;
import github.tdonuk.notemanager.util.EnvironmentUtils;
import github.tdonuk.notemanager.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
	private FileHist fileHist;
	private boolean isTemporary; // to determine whether opened file is actually exists and user has opened it to edit, or this tab has created as 'New Tab' with empty content that has no real file.
	private boolean isDuplicated = false; // to determine whether the title will be the file name (file.txt) or the absolute path of the file
	
	public EditorTab(FileHist fileHist, boolean isTemporary) throws IOException {
		super(new BorderLayout());
		
		if(fileHist == null) throw new CustomException("fileHist should not be null");
		
		this.title = fileHist.getTitle();
		this.fileHist = fileHist;
		this.isTemporary = isTemporary;

		init();
		
		File openedFile = new File(fileHist.getOpenedFile());
		
		String fileName = openedFile.getName();

		if(openedFile.getName().contains(".")) {
			int indexOfLastDot = fileName.lastIndexOf(".");
			type = FileType.findByExtension(fileName.substring(indexOfLastDot));
		} else type = FileType.TXT;
		
		if(!isTemporary) editorContainer.getEditorPane().getEditor().setText(loadFileContent());
		
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
	
	public FileHist save() throws IOException {
		log.info("saving tab: " + fileHist.getOpenedFile());
		if(!isTemporary) {
			Files.write(Paths.get(fileHist.getOpenedFile()), editorContainer.getEditorPane().getEditor().getText().getBytes());
			
			return fileHist;
		} else {
			File newFile = DialogUtils.askFileForSave(new File(fileHist.getOpenedFile()));
			
			if(newFile != null) {
				if(!newFile.canWrite() && !newFile.createNewFile()) throw new CustomException("could not able to create file to save");
				
				Files.write(newFile.toPath(), editorContainer.getEditorPane().getEditor().getText().getBytes());
				
				fileHist.setOpenedFile(newFile.getAbsolutePath());
				
				this.isTemporary = false;
				
				return fileHist;
			}
			else return null;
		}
	}
	
	public void format() {
		Editor editor = editorContainer.getEditorPane().getEditor();
		
		log.info("formatting " + type.name().toLowerCase());
		
		if (editor.getText() == null || editor.getText().isBlank()) return;
		
		try {
			editor.setText(StringUtils.formatWithType(editor.getText(), type));
		} catch (Exception ex) {
			throw new CustomException(ex);
		}
	}
	
	public void reload(boolean refreshContent) throws IOException {
		if(!isTemporary) {
			File openedFile = new File(fileHist.getOpenedFile());
			if(!openedFile.canRead()) { // tab is not temporary but still cannot read from its file
				EnvironmentUtils.beep();
				boolean isApproved = DialogUtils.askApprovation("The file has deleted or not able to read it's content. Do you want to remove this tab?", "Can not read content");
				
				if(isApproved) {
					removeSelf();
					return;
				} else isTemporary = true; // from now on, this tab will be treated as temporary because it has no file to read from
			}
			
			if(refreshContent) {
				if(hasDiff()) {
					boolean isApproved = DialogUtils.askApprovation("You have unsaved changes will be overwrite after this operation. Do you approve?", "You unsaved changes");
					
					if(!isApproved) return;
				}
				editorContainer.getEditorPane().getEditor().setText(loadFileContent());
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
		headerPanel.setOpaque(false);
		headerPanel.setBackground(null);
		
		JButton closeButton = new JButton("X");
		closeButton.addActionListener(a -> removeSelf());
		closeButton.setBorder(null);
		closeButton.setContentAreaFilled(false);
		
		JLabel tabTitle = new JLabel(isDuplicated ? fileHist.getOpenedFile() : title, FileSystemView.getFileSystemView().getSystemIcon(new File(fileHist.getOpenedFile())), SwingConstants.LEADING);
		
		Font font = tabTitle.getFont().deriveFont(Font.BOLD);
		
		if(hasDiff()) tabTitle.setFont(font);
		
		headerPanel.add(closeButton, BorderLayout.EAST);
		headerPanel.add(tabTitle,BorderLayout.WEST);
		
		return headerPanel;
	}
	
	public boolean hasDiff() {
		try {
			if(isTemporary) return !getEditorContainer().getEditorPane().getEditor().getText().isBlank();
			else { // tab is not temp
				return ! loadFileContent().equals(editorContainer.getEditorPane().getEditor().getText());
			}
		} catch(Exception e) {
			log.error("exception happened while checking for differences: " + e.getMessage(), e);
			DialogUtils.showError("Cannot check for diff: " + e.getMessage(), "Diff Check Failed");
			return true;
		}
	}
	
	public String loadFileContent() throws IOException {
		return Files.readString(Paths.get(fileHist.getOpenedFile()));
	}
	
}
