package gui.component;

import constant.Application;

import javax.swing.*;

public class PrimaryLabel extends JLabel {
	public PrimaryLabel(String title) {
		super(title);
		
		this.setFont(Application.PRIMARY_FONT);
	}
}
