package github.tdonuk.notemanager.gui.container;

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
		this.setBorder(new EmptyBorder(2,2,2,2));
	}
}
