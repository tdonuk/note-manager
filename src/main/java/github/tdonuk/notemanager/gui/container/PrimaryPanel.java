package gui.container;

import gui.theme.PaletteHolder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PrimaryPanel extends JPanel {
	public PrimaryPanel() {
		super();
		init();
	}
	
	public PrimaryPanel(LayoutManager2 layoutManager) {
		super(layoutManager);
		init();
	}
	
	private void init() {
		// this.setBackground(PaletteHolder.PRIMARY_BACKGROUND.getValue());
		
		this.setBorder(new EmptyBorder(2,2,2,2));
	}
}
