package github.tdonuk.notemanager.gui.container;

import github.tdonuk.notemanager.constant.FileType;
import github.tdonuk.notemanager.gui.component.Editor;
import lombok.Getter;
import lombok.Setter;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.nio.file.Files;

/**
 * Represents the tabs of the tabbed pane
 */
@Getter
@Setter
public class EditorTab extends JPanel {
	private EditorContainer editorContainer;

	private String title;
	private FileType type;
	private File openedFile;
	
	public EditorTab(File file) {
		super(new BorderLayout());
		
		this.title = file.getName();
		this.openedFile = file;

		init();

		if(title.contains(".")) {
			type = FileType.findByExtension(title.substring(title.lastIndexOf(".")));
		} else type = FileType.TXT;
		
		determineSyntaxHighlighting();
	}

	public EditorTab(String tabName) {
		super(new BorderLayout());

		this.title = tabName;

		init();
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

		this.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println("tab opened: " + title);
			}

			@Override
			public void focusLost(FocusEvent e) {
				System.out.println("tab closed: " + title);
			}
		});
	}
	
}
