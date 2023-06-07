package github.tdonuk.notemanager.gui.component;

import github.tdonuk.notemanager.exception.CustomException;
import github.tdonuk.notemanager.gui.MainWindow;
import github.tdonuk.notemanager.gui.constant.EditorState;
import github.tdonuk.notemanager.gui.container.EditorTab;
import github.tdonuk.notemanager.util.DialogUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
public class EditorTabManager extends JTabbedPane {
	private final Map<File, EditorTab> tabs = new HashMap<>();
	
	public EditorTabManager() {
		super();
		
		this.addChangeListener(e -> {
			EditorTabManager tabbedPane = (EditorTabManager) e.getSource();
			
			EditorTab selectedTab = tabbedPane.getSelectedComponent();
			
			if(selectedTab != null) {
				log.info("selected tab: " + selectedTab.getTitle());
				
				try {
					reloadTab(selectedTab);
				} catch(IOException ex) {
					throw new CustomException(ex);
				}
			}
		});
	}
	
	public EditorTab addTab(@NonNull File file) throws IOException {
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
		
		return tabToAdd;
	}
	
	public EditorTab addTab(@NonNull String title) throws IOException {
		title = makeTitleUnique(title);
		
		return addTab(new File(title));
	}
	
	@Override
	public void setTitleAt(int index, String title) { // updates the tab header component (the small components at the top of the tabbed pane container that has the tab titles in it)
		setTabComponentAt(index, ((EditorTab) getComponentAt(index)).getHeader());
	}
	
	@Override
	public Component add(String title, Component component) {
		EditorTab tab = (EditorTab) component;
		
		Component added = super.add(title, tab);
		tabs.put(tab.getOpenedFile(), tab);
		
		setTabComponentAt(indexOfComponent(added), ((EditorTab) added).getHeader());
		
		return added;
	}
	
	@Override
	public void remove(int index) {
		EditorTab toDelete = (EditorTab) getComponentAt(index);
		
		if(toDelete.hasDiff()) {
			boolean accepted = DialogUtils.askConfirmation("Your unsaved changes will be lost. Still close this tab?", "Unsaved Changes");
			if(!accepted) return;
		}
		
		super.remove(index);
		
		tabs.values().remove(toDelete);
		
		for(EditorTab tab : tabs.values()) {
 			if(getTabsWithFileName(tab.getOpenedFile().getName()).size() == 1) {
				tab.setTitle(tab.getOpenedFile().getName());
				setTabComponentAt(indexOfComponent(tab), tab.getHeader());
			}
		}
	}
	
	@Override
	public EditorTab getSelectedComponent() {
		return (EditorTab) super.getSelectedComponent();
	}
	
	
	public EditorTab getTab(File file) {
		return tabs.get(file);
	}
	
	public List<EditorTab> getTabsWithFileName(String fileName) {
		return tabs.values().stream().filter(tab -> tab.getOpenedFile().getName().equals(fileName)).toList();
	}
	
	public void setSelectedTab(EditorTab tab) {
		this.setSelectedIndex(indexOfComponent(tab));
	}
	
	private String makeTitleUnique(String title) {
		String titleWithIndex = title + ".txt";
		int index = 0;
		
		while(existsWithFileName(titleWithIndex)) {
			titleWithIndex = title + " (" + (++index) + ").txt";
		}
		
		return titleWithIndex;
	}
	
	private String makeTitleUnique(File file) {
		return existsWithFileName(file.getName()) ? file.getName() + " ("+ file.getAbsolutePath() +")" : file.getName();
	}
	
	public boolean existsWithFileName(String fileName) {
		return tabs.values().stream().anyMatch(tab -> tab.getOpenedFile().getName().equals(fileName));
	}
	
	public void saveTab(EditorTab tab) {
		log.info("saving " + tab.getOpenedFile().getAbsolutePath());
		
		MainWindow.updateState(EditorState.SAVING);
		
		try {
			File savedFile = tab.save();
			
			if(savedFile != null) {
				if(!tabs.containsKey(savedFile)) {
					tabs.values().remove(tab);
					tabs.put(savedFile, tab);
					reloadTab(tab);
				}
				
				log.info("saved to: " + savedFile.getAbsolutePath());
			}
			
		} catch(IOException ex) {
			throw new CustomException(ex);
		}
		
		MainWindow.updateState(EditorState.READY);
	}
	
	public void reloadTab(EditorTab tab) throws IOException {
		getSelectedComponent().reload(false);
		if(tab.isTempFlag()) setTabComponentAt(indexOfComponent(tab), tab.getHeader());
	}
	
}
