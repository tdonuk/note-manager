package github.tdonuk.notemanager.gui.container;

import github.tdonuk.notemanager.gui.component.Label;
import github.tdonuk.notemanager.gui.event.CommonEventListeners;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class EditorTabPane extends JTabbedPane {
	private final List<EditorTab> tabs = new ArrayList<>();
	
	public EditorTabPane() {
		super();
		
		if(tabs.isEmpty()) addTab("New Document");
	}
	
	public EditorTab addTab(File file) {
		if(exists(file)) { // user opens the same file that previously opened
			EditorTab duplicate = getTab(file);

			setSelectedTab(duplicate);

			return duplicate;
		}

		String title = file.getName();

		if(exists(file.getName())) { // user opens a file that a tab already exists with this filename but with a different file
			title = file.getAbsolutePath();

			EditorTab duplicate = getTab(file);
			if(duplicate.getOpenedFile() != null) {
				duplicate.setTitle(duplicate.getOpenedFile().getAbsolutePath());
				setTitleAt(indexOfTab(file.getName()), duplicate.getOpenedFile().getAbsolutePath());
			}
		}

		EditorTab editorTab = new EditorTab(file);
		editorTab.setTitle(title);
		
		tabs.add(editorTab);
		this.addTab(title, editorTab);
		
		this.setTabComponentAt(this.indexOfTab(title), titlePanel(editorTab));

		System.out.println(this.getTabComponentAt(indexOfTab(title)));
		
		return editorTab;
	}

	public EditorTab addTab(String title) {
		String titleToShow = makeTitleUnique(title);

		EditorTab editorTab = new EditorTab(titleToShow);

		tabs.add(editorTab);
		this.addTab(editorTab.getTitle(), editorTab);

		this.setTabComponentAt(this.indexOfTab(titleToShow), titlePanel(editorTab));

		return editorTab;
	}

	private String makeTitleUnique(String title) {
		int index = 0;
		String titleToShow = title;
		while(exists(titleToShow)) {
			titleToShow = title + " (" + (++index) + ")";
		}

		return titleToShow;
	}
	
	public EditorTab getTab(String title) {
		return tabs.stream().filter(tab -> title.equals(tab.getTitle())).findAny().orElse(null);
	}

	public EditorTab getTab(File file) {
		return tabs.stream().filter(tab -> tab.getOpenedFile() != null && file.getAbsolutePath().equals(tab.getOpenedFile().getAbsolutePath())).findAny().orElse(null);
	}
	
	public EditorTab getSelectedTab() {
		return (EditorTab) this.getSelectedComponent();
	}
	
	public void setSelectedTab(EditorTab tab) {
		this.setSelectedIndex(this.indexOfComponent(tab));
	}
	
	public void setSelectedTab(String title) {
		this.setSelectedIndex(this.indexOfTab(title));
	}
	
	public boolean exists(String title) {
		return this.tabs.stream().anyMatch(tab -> (tab.getTitle().equals(title)) || (tab.getOpenedFile() != null && tab.getOpenedFile().getName().equals(title)));
	}

	public boolean exists(File file) {
		return (exists(file.getName()) || exists(file.getAbsolutePath())) && tabs.stream().map(EditorTab::getOpenedFile).anyMatch(f -> f != null && f.getAbsolutePath().equals(file.getAbsolutePath()));
	}
	
	private JPanel titlePanel(EditorTab tab) {
		JPanel panel = new JPanel(new BorderLayout(5, 0));
		JLabel label = new Label(tab.getTitle());
		JLabel close = new Label("x");
		
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

	@Override
	public void setTitleAt(int index, String title) {
		setTabComponentAt(index, titlePanel((EditorTab) getComponentAt(index)));
	}
}
