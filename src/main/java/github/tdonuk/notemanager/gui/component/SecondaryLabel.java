package gui.component;

import constant.Application;
import gui.theme.PaletteHolder;

import javax.swing.*;

public class SecondaryLabel extends JLabel {
	public SecondaryLabel(String text) {
		super(text);
		
		this.setFont(Application.SECONDARY_FONT);
		this.setForeground(PaletteHolder.SECONDARY_FOREGROUND.getValue());
	}
}
