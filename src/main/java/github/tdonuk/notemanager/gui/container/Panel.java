package github.tdonuk.notemanager.gui.container;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Panel extends JPanel {
	public Panel() {
		super();
		init();
	}
	
	public Panel(LayoutManager2 layoutManager) {
		super(layoutManager);
		init();
	}
	
	private void init() {
		this.setBorder(new EmptyBorder(2,2,2,2)); // padding
	}
}
