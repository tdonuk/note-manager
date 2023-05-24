package github.tdonuk.notemanager.gui.container;

import github.tdonuk.notemanager.gui.component.Editor;
import lombok.Getter;
import org.fife.ui.rtextarea.RTextScrollPane;

@Getter
public class EditorScrollPane extends RTextScrollPane {
	private final Editor editor;
	
	public EditorScrollPane() {
		super();
		
		this.editor = new Editor();
		
		this.setViewportView(editor);
	}
}
