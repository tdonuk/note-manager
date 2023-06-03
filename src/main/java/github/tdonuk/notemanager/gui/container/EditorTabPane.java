package github.tdonuk.notemanager.gui.container;

import github.tdonuk.notemanager.constant.Application;
import github.tdonuk.notemanager.gui.constant.EditorShortcut;
import lombok.Getter;
import lombok.NonNull;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class EditorTabPane extends JTabbedPane {
	private final Map<File, EditorTab> tabs = new HashMap<>();
	
	public static EditorTab SELECTED_TAB = null;
	
	public EditorTabPane() {
		super();
		
		this.setFont(Application.PRIMARY_FONT);
		
		this.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				EditorTabPane tabbedPane = (EditorTabPane) e.getSource();
				
				SELECTED_TAB = (EditorTab) tabbedPane.getSelectedComponent();
				System.out.println("selected tab: " + SELECTED_TAB.getTitle());
			}
		});
		
		if(tabs.isEmpty()) addTab("New Document");
	}
	
	public EditorTab addTab(@NonNull File file) {
		if(tabs.containsKey(file)) { // user opens a file that is already opened in a tab
			setSelectedTab(tabs.get(file));
			return tabs.get(file);
		}
		
		String title = makeTitleUnique(file);
		
		if(existsWithFileName(file.getName())) { // user opens a file with name matching one of the already opened files filename
			List<EditorTab> duplicates = getTabsWithFileName(file.getName());
			duplicates.forEach(duplicate -> {
				duplicate.setTitle(duplicate.getOpenedFile().getName() + " ("+duplicate.getOpenedFile().getAbsolutePath()+")");
				setTitleAt(indexOfComponent(duplicate), duplicate.getTitle());
			});
		}
		
		EditorTab tabToAdd = new EditorTab(file, title);
		
		this.add(title, tabToAdd);
		
		tabs.put(file, tabToAdd);
		
		return tabToAdd;
	}
	
	@Override
	public void setTitleAt(int index, String title) {
		setTabComponentAt(index, ((EditorTab) getComponentAt(index)).getHeader());
	}
	
	@Override
	public Component add(String title, Component component) {
		Component added = super.add(title, component);
		
		setTabComponentAt(indexOfComponent(added), ((EditorTab) added).getHeader());
		
		((EditorTab) added).registerKeyboardAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: do control for unsaved changes
				remove(indexOfComponent(added));
			}
		}, EditorShortcut.CLOSE.getKeyStroke(), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		
		return added;
	}
	
	@Override
	public void remove(int index) {
		if(tabs.size() < 2) return;
		
		EditorTab toDelete = (EditorTab) getComponentAt(index);
		
		super.remove(index);
		
		tabs.remove(toDelete.getOpenedFile());
		
		for(EditorTab tab : tabs.values()) {
 			if(getTabsWithFileName(tab.getOpenedFile().getName()).size() == 1) {
				tab.setTitle(tab.getOpenedFile().getName());
				setTabComponentAt(indexOfComponent(tab), tab.getHeader());
			}
		}
	}
	
	public EditorTab addTab(String title) {
		title = makeTitleUnique(title);
		
		EditorTab tabToAdd = new EditorTab(new File(title), title);
		
		this.add(title, tabToAdd);
		
		tabs.put(tabToAdd.getOpenedFile(), tabToAdd);
		
		return tabToAdd;
	}
	
	public EditorTab getTab(File file) {
		return tabs.get(file);
	}
	
	public List<EditorTab> getTabsWithFileName(String fileName) {
		return tabs.values().stream().filter(tab -> tab.getOpenedFile().getName().equals(fileName)).collect(Collectors.toList());
	}
	
	public void setSelectedTab(EditorTab tab) {
		this.setSelectedIndex(indexOfComponent(tab));
	}
	
	private String makeTitleUnique(String title) {
		String titleWithIndex = title;
		int index = 0;
		
		while(exists(titleWithIndex)) {
			titleWithIndex = title + " (" + (++index) + ")";
		}
		
		return titleWithIndex;
	}
	
	private String makeTitleUnique(File file) {
		return existsWithFileName(file.getName()) ? file.getName() + " ("+ file.getAbsolutePath() +")" : file.getName();
	}
	
	public boolean exists(String title) {
		return indexOfTab(title) != -1;
	}
	
	public boolean existsWithFileName(String fileName) {
		return tabs.values().stream().anyMatch(tab -> tab.getOpenedFile().getName().equals(fileName));
	}
	
}
