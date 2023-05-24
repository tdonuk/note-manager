package gui.container;

import constant.FileType;
import gui.container.EditorContainer;
import gui.theme.PaletteHolder;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the tabs of the tabbed pane
 */
@Getter
public class EditorTab extends JPanel {
	private EditorContainer editorContainer;
	private String title;
	
	public EditorTab(String title) {
		super(new BorderLayout());
		
		this.title = title;
		
		// this.setBackground(PaletteHolder.SECONDARY_BACKGROUND.getValue());
		
		this.editorContainer = new EditorContainer();
		this.add(editorContainer);
	}
	
	public FileType getFileType() {
		if(!title.contains(".")) return FileType.TXT;
		
		return FileType.findByExtension(title.substring(title.indexOf(".")+1));
	}
	
	
}
