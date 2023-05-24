package gui.container;

import gui.component.Editor;
import gui.theme.PaletteHolder;
import lombok.Getter;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;

@Getter
public class EditorScrollPane extends RTextScrollPane {
	private Editor editor;
	
	public EditorScrollPane() {
		super();
		
		this.editor = new Editor();
		
		this.setViewportView(editor);
	}
}
