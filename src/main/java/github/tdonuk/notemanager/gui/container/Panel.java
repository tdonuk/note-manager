package github.tdonuk.notemanager.gui.container;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Panel extends JPanel {
	public Panel() {
		super(new FlowLayout(FlowLayout.CENTER, 2,2));
		init();
	}
	
	public Panel(LayoutManager2 layoutManager) {
		super(layoutManager);
		init();
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		
		for(Component component : this.getComponents()) {
			component.setFont(font);
		}
	}
	
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		
		for(Component component : this.getComponents()) {
			component.setForeground(fg);
		}
	}
	
	private void init() {
		this.setBorder(new EmptyBorder(2,2,2,2)); // padding
	}
}
