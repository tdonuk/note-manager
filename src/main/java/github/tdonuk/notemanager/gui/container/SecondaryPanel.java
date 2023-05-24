package gui.container;

import gui.theme.PaletteHolder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SecondaryPanel extends JPanel {
	public SecondaryPanel() {
		super();
		init();
	}
	
	public SecondaryPanel(LayoutManager2 layoutManager) {
		super(layoutManager);
		init();
	}
	
	private void init() {
		// this.setBackground(PaletteHolder.SECONDARY_BACKGROUND.getValue());
		this.setBorder(new EmptyBorder(2,2,2,2));
	}
}
