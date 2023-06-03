package github.tdonuk.notemanager.gui.container;

import github.tdonuk.notemanager.constant.FileType;
import github.tdonuk.notemanager.gui.component.Editor;
import github.tdonuk.notemanager.gui.component.EditorTabPane;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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
	
	public EditorTab(@NonNull File file, String title) {
		super(new BorderLayout());
		
		this.title = title;
		this.openedFile = file;

		init();

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
