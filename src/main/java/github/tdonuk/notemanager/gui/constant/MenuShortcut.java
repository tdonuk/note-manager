package github.tdonuk.notemanager.gui.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@Getter
@AllArgsConstructor
public enum MenuShortcut {
	NEW("CTRL + N", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)),
	OPEN("CTRL + T", KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
	
	private final String label;
	private final KeyStroke keyStroke;
}
