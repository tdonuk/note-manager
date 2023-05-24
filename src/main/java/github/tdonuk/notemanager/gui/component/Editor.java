package gui.component;

import constant.Application;
import lombok.SneakyThrows;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class Editor extends RSyntaxTextArea {

	@SneakyThrows
	public Editor() {
		super();
		
		setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		
		/*
		this.setBackground(PaletteHolder.PRIMARY_BACKGROUND.getValue());
		this.setForeground(PaletteHolder.PRIMARY_FOREGROUND.getValue());
		 */
		this.setFont(Application.PRIMARY_FONT);
	}
}
