package github.tdonuk.notemanager.gui.container;

import github.tdonuk.notemanager.constant.Application;
import github.tdonuk.notemanager.gui.component.PrimaryLabel;
import github.tdonuk.notemanager.gui.event.CommonEventListeners;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class EditorTabPane extends JTabbedPane {
	private List<EditorTab> tabs = new ArrayList<>();
	
	public EditorTabPane() {
		super();
		
		/*
		this.setForeground(PaletteHolder.SECONDARY_FOREGROUND.getValue());
		this.setBackground(PaletteHolder.SECONDARY_BACKGROUND.getValue());
		 */
		
		this.setFont(Application.SECONDARY_FONT);
		
		if(tabs.isEmpty()) addTab("New Document");
	}
	
	public EditorTab addTab(String title) {
		int index = 0;
		String titleToShow = title;
		while(exists(titleToShow)) {
			titleToShow = title + " (" + (++index) + ")";
		}
		
		EditorTab editorTab = new EditorTab(titleToShow);
		
		tabs.add(editorTab);
		this.addTab(titleToShow, editorTab);
		
		this.setTabComponentAt(this.indexOfTab(titleToShow), titlePanel(editorTab));
		
		return editorTab;
	}
	
	public EditorTab getTabWithTitle(String title) {
		return tabs.stream().filter(tab -> title.equals(tab.getTitle())).findAny().orElse(null);
	}
	
	public EditorTab getSelectedTab() {
		return (EditorTab) this.getSelectedComponent();
	}
	
	public void setSelectedTab(EditorTab tab) {
		this.setSelectedIndex(this.indexOfComponent(tab));
	}
	
	public boolean exists(String title) {
		return this.indexOfTab(title) != -1;
	}
	
	private JPanel titlePanel(EditorTab tab) {
		JPanel panel = new JPanel(new BorderLayout(5, 0));
		JLabel label = new PrimaryLabel(tab.getTitle());
		JLabel close = new PrimaryLabel("x");
		
		panel.setBorder(new EmptyBorder(0,2,0,2));
		
		close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		close.addMouseListener(CommonEventListeners.mouseClicked(e -> {
			if(tabs.size() != 1) {
				tabs.remove(tab);
				this.remove(this.indexOfTab(tab.getTitle()));
			}
		}));
		
		panel.add(label, BorderLayout.WEST);
		panel.add(close, BorderLayout.EAST);
		
		panel.setBackground(new Color(0,0,0, 0));
		
		return panel;
	}
}
