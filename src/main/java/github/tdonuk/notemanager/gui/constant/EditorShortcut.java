package github.tdonuk.notemanager.gui.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@Getter
@AllArgsConstructor
public enum EditorShortcut {
    FORMAT("CTRL + Shift + F", KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.SHIFT_DOWN_MASK|InputEvent.CTRL_DOWN_MASK), "formatText"),
    UNDO("CTRL + Z", KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo"),
    CLOSE("CTRL + W", KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK), "closeTab"),
    SAVE("CTRL + S", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "save");

    private final String label;
    private final KeyStroke keyStroke;
    private final String operation;
}
