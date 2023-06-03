package github.tdonuk.notemanager.gui.component;

import github.tdonuk.notemanager.gui.constant.EditorShortcut;
import lombok.SneakyThrows;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class Editor extends RSyntaxTextArea {

    @SneakyThrows
    public Editor() {
        super();
        
        /*
        * map shortcuts to operation names. example: ctrl+shift+f -> "format"
        * this operation names will be inserted of this component's actionMap as an action key
        */
        this.getInputMap().put(EditorShortcut.FORMAT.getKeyStroke(), EditorShortcut.FORMAT.getOperation());
        this.getInputMap().put(EditorShortcut.SAVE.getKeyStroke(), EditorShortcut.SAVE.getOperation());
        this.getInputMap().put(EditorShortcut.CLOSE.getKeyStroke(), EditorShortcut.CLOSE.getOperation());
    }
}
