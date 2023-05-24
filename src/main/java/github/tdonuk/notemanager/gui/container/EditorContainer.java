package gui.container;

import gui.component.Editor;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class EditorContainer extends JPanel {
	private EditorScrollPane editorPane;
	
	public EditorContainer() {
		super(new BorderLayout());
		
		// this.setBackground(PaletteHolder.SECONDARY_BACKGROUND.getValue());
		
		this.editorPane = new EditorScrollPane();
		
		this.add(editorPane);
	}
}
