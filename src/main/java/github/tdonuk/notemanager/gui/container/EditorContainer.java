package github.tdonuk.notemanager.gui.container;

import github.tdonuk.notemanager.gui.component.EditorScrollPane;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class EditorContainer extends JPanel {
	private final EditorScrollPane editorPane;
	
	public EditorContainer() {
		super(new BorderLayout());
		
		this.editorPane = new EditorScrollPane();
		
		this.add(editorPane);
	}
}
