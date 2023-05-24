package github.tdonuk.notemanager.gui.component;

import github.tdonuk.notemanager.constant.Application;
import github.tdonuk.notemanager.gui.theme.PaletteHolder;

import javax.swing.*;

public class SecondaryLabel extends JLabel {
	public SecondaryLabel(String text) {
		super(text);
		
		this.setFont(Application.SECONDARY_FONT);
	}
}
