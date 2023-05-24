package github.tdonuk.notemanager.gui.container;

import github.tdonuk.notemanager.constant.FileType;
import github.tdonuk.notemanager.gui.component.Editor;
import lombok.Getter;
import lombok.Setter;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the tabs of the tabbed pane
 */
@Getter
@Setter
public class EditorTab extends JPanel {
	private EditorContainer editorContainer;
	private String title;
	
	public EditorTab(String title, FileType type) {
		super(new BorderLayout());
		
		this.title = title;
		
		// this.setBackground(PaletteHolder.SECONDARY_BACKGROUND.getValue());
		
		this.editorContainer = new EditorContainer();
		this.add(editorContainer);
		
		Editor editor = editorContainer.getEditorPane().getEditor();
		
		if(type == null) type = FileType.TXT;
		
		switch(type) {
			case XML, XSL, XSLT -> {
				editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
			}
			case HTML -> {
				editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
			}
			case JSON -> {
				editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
			}
			default -> {
				editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
			}
		}
	}
	
	public FileType getFileType() {
		if(!title.contains(".")) return FileType.TXT;
		
		return FileType.findByExtension(title.substring(title.indexOf(".")+1));
	}
	
}
