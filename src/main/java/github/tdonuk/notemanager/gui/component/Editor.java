package github.tdonuk.notemanager.gui.component;

import github.tdonuk.notemanager.constant.Application;
import github.tdonuk.notemanager.gui.constant.EditorShortcut;
import github.tdonuk.notemanager.util.StringUtils;
import lombok.SneakyThrows;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;

public class Editor extends RSyntaxTextArea {

    @SneakyThrows
    public Editor() {
        super();

        init();
        initTheme();
        initShortcuts();
    }

    private void initShortcuts() {
        final Editor editor = this;

        this.getInputMap().put(EditorShortcut.FORMAT.getKeyStroke(), "format");
        this.getActionMap().put("format", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("formatting " + editor.getSyntaxEditingStyle());
                        if (editor.getText() == null || editor.getText().isBlank()) return;
                        try {
                            if (SyntaxConstants.SYNTAX_STYLE_XML.equals(editor.getSyntaxEditingStyle()))
                                editor.setText(StringUtils.formatXml(editor.getText()));
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
    }

    private void initTheme() {
        this.setFont(Application.PRIMARY_FONT);
    }
}
