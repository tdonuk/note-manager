package github.tdonuk.notemanager.gui.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@Getter
@AllArgsConstructor
public enum EditorShortcut {
    FORMAT("CTRL + Shift + F", KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.SHIFT_DOWN_MASK|InputEvent.CTRL_DOWN_MASK)),
    UNDO("CTRL + Z", KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK)),
    CLOSE("CTRL + W", KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));

    private final String label;
    private final KeyStroke keyStroke;
}
