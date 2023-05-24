package gui.component;

import constant.Application;
import gui.theme.PaletteHolder;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SecondaryButton extends JButton {
	public SecondaryButton(String title) {
		super(title);
		init();
	}
	
	public SecondaryButton(String title, ActionListener listener) {
		super(title);
		init();
		
		this.addActionListener(listener);
	}
	
	private void init() {
		/*
		this.setBackground(PaletteHolder.SECONDARY_BACKGROUND.getValue());
		this.setForeground(PaletteHolder.SECONDARY_FOREGROUND.getValue());
		this.setFont(Application.SECONDARY_FONT);
		 */
	}
}
