package github.tdonuk.notemanager.gui.component;

import github.tdonuk.notemanager.constant.Application;
import lombok.SneakyThrows;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class Editor extends RSyntaxTextArea {

	@SneakyThrows
	public Editor() {
		super();

		this.setFont(Application.PRIMARY_FONT);
	}
}
