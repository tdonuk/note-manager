package github.tdonuk.notemanager.gui.container;

import github.tdonuk.notemanager.constant.FileType;
import github.tdonuk.notemanager.exception.CustomException;
import github.tdonuk.notemanager.gui.component.Editor;
import github.tdonuk.notemanager.gui.component.EditorTabPane;
import github.tdonuk.notemanager.util.DialogUtils;
import github.tdonuk.notemanager.util.StringUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
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
	
	public EditorTab(@NonNull File file, String title) throws IOException {
		super(new BorderLayout());
		
		this.title = title;
		this.openedFile = file;

		init();
		
		reload();

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
		} catch (Exception ex) {
			throw new CustomException(ex);
		}
	}
	
	public void reload() throws IOException {
		if(openedFile.canRead()) {
			editorContainer.getEditorPane().getEditor().setText(Files.readString(openedFile.toPath()));
		}
	}
	
	// title component of the tab
	public JPanel getHeader() {
		JPanel panel = new Panel(new BorderLayout(5, 5));
		panel.add(new JLabel(title),BorderLayout.WEST);
		
		JButton closeButton = new JButton("X");
		closeButton.addActionListener(a -> {
			EditorTabPane parent = (EditorTabPane) this.getParent();
			parent.remove(parent.indexOfComponent(this));
		});
		
		panel.setOpaque(false);
		panel.setBackground(null);
		
		closeButton.setBorder(null);
		setBackground(null);
		setOpaque(false);
		
		panel.add(closeButton, BorderLayout.EAST);
		
		return panel;
	}
	
}
