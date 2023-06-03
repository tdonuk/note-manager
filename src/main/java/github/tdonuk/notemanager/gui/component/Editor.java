package github.tdonuk.notemanager.gui.component;

import github.tdonuk.notemanager.constant.Application;
import github.tdonuk.notemanager.gui.constant.EditorShortcut;
import lombok.SneakyThrows;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class Editor extends RSyntaxTextArea {

    @SneakyThrows
    public Editor() {
        super();

        init();
        initTheme();
        
        /*
        * map shortcuts to operation names. example: ctrl+shift+f -> "format"
        * where this "format" expression can be inserted of this component's actionMap at somewhere else
        */
        this.getInputMap().put(EditorShortcut.FORMAT.getKeyStroke(), EditorShortcut.FORMAT.getOperation());
        this.getInputMap().put(EditorShortcut.SAVE.getKeyStroke(), EditorShortcut.SAVE.getOperation());
        this.getInputMap().put(EditorShortcut.CLOSE.getKeyStroke(), EditorShortcut.CLOSE.getOperation());
    }

    private void initTheme() {
        this.setFont(Application.PRIMARY_FONT);
    }
}
