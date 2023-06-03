package github.tdonuk.notemanager.gui.component;

import lombok.SneakyThrows;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.awt.event.ActionEvent;

public class Editor extends RSyntaxTextArea {

    @SneakyThrows
    public Editor() {
        super();
        
        this.addCaretListener(e -> {
            // System.out.println("caret updated: " + this.getCaretLineNumber() + " x " + this.getCaretOffsetFromLineStart());
            // TODO: show caret line and column at the footer of the main window
        });
    }
    
    public void undo() {
        this.getActionMap().get("RTA.UndoAction").actionPerformed(new ActionEvent(this, 0, "undo"));
    }
    
    public void redo() {
        this.getActionMap().get("RTA.RedoAction").actionPerformed(new ActionEvent(this, 1, "redo"));
    }
}
