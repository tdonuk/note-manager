package github.tdonuk.notemanager.gui.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@Getter
@AllArgsConstructor
public enum MenuShortcut {
	// File Menu
	NEW("CTRL + N", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "newTab"),
	OPEN("CTRL + T", KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK), "openFile"),
	
	// Edit Menu
	SAVE("CTRL + S", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "save"),
	FORMAT("CTRL + Shift + F", KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.SHIFT_DOWN_MASK|InputEvent.CTRL_DOWN_MASK), "format"),
	REFRESH("CTRL + F5", KeyStroke.getKeyStroke(KeyEvent.VK_F5, InputEvent.CTRL_DOWN_MASK), "refresh"),
	UNDO("CTRL + Z", KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo"),
	REDO("CTRL + Y", KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redo"),
	CLOSE("CTRL + W", KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK), "closeTab");
	
	private final String label;
	private final KeyStroke keyStroke;
	private final String operationName;
}
