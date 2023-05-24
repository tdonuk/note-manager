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
	
	public EditorTab(String title) {
		super(new BorderLayout());
		
		this.title = title;
		
		// this.setBackground(PaletteHolder.SECONDARY_BACKGROUND.getValue());
		
		this.editorContainer = new EditorContainer();
		this.add(editorContainer);
		
		Editor editor = editorContainer.getEditorPane().getEditor();
		
		if(title.contains(".")) {
			String extension = title.substring(title.indexOf("."));
			
			FileType type = FileType.findByExtension(extension);
			
			switch(type) {
				case XML -> {
					editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
				}
				case HTML -> {
					editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
				}
				case TXT -> {
					editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
				}
				case JSON -> {
					editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
				}
			}
		} else {
			editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		}
	}
	
	public FileType getFileType() {
		if(!title.contains(".")) return FileType.TXT;
		
		return FileType.findByExtension(title.substring(title.indexOf(".")+1));
	}
	
}
