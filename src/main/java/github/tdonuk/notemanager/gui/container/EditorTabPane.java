package github.tdonuk.notemanager.gui.container;

import github.tdonuk.notemanager.constant.Application;
import github.tdonuk.notemanager.gui.MainWindow;
import github.tdonuk.notemanager.gui.component.Editor;
import github.tdonuk.notemanager.gui.constant.EditorShortcut;
import github.tdonuk.notemanager.gui.constant.EditorState;
import github.tdonuk.notemanager.util.DialogUtils;
import github.tdonuk.notemanager.util.StringUtils;
import lombok.Getter;
import lombok.NonNull;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class EditorTabPane extends JTabbedPane {
	private final Map<File, EditorTab> tabs = new HashMap<>();
	
	public EditorTabPane() {
		super();
		
		this.setFont(Application.PRIMARY_FONT);
		
		this.addChangeListener(e -> {
			EditorTabPane tabbedPane = (EditorTabPane) e.getSource();
			
			System.out.println("selected tab: " + tabbedPane.getSelectedComponent().getTitle());
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
		
		return tabToAdd;
	}
	
	public EditorTab addTab(@NonNull String title) {
		title = makeTitleUnique(title);
		
		EditorTab tabToAdd = new EditorTab(new File(title+".txt"), title);
		
		this.add(title, tabToAdd);
		
		return tabToAdd;
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
		
		initTabShortcuts((EditorTab) added);
		
		return added;
	}
	
	@Override
	public void remove(int index) {
		if(tabs.size() < 2) return;
		
		// TODO: do control for unsaved changes
		
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
	
	@Override
	public EditorTab getSelectedComponent() {
		return (EditorTab) super.getSelectedComponent();
	}
	
	private void initTabShortcuts(EditorTab tab) {
		// shortcuts for editor operations. like save, format etc..
		
		Editor editor = tab.getEditorContainer().getEditorPane().getEditor();
		File tabFile = tab.getOpenedFile();
		editor.getActionMap().put(EditorShortcut.FORMAT.getOperation(), new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("formatting " + editor.getSyntaxEditingStyle());
						if (editor.getText() == null || editor.getText().isBlank()) return;
						try {
							if (SyntaxConstants.SYNTAX_STYLE_XML.equals(editor.getSyntaxEditingStyle())) editor.setText(StringUtils.formatXml(editor.getText()));
						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					}
				}
		);
		
		editor.getActionMap().put(EditorShortcut.CLOSE.getOperation(), new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						remove(indexOfComponent(tab));
					}
				}
		);
		
		editor.getActionMap().put(EditorShortcut.SAVE.getOperation(), new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("saving " + tabFile.getAbsolutePath());
						
						MainWindow.updateState(EditorState.SAVING);
						
						try {
							if(tabFile.canWrite()) {
								Files.write(tabFile.toPath(), editor.getText().getBytes());
							}
							else {
								File file = DialogUtils.askForSave(tabFile);
								
								if(file != null) {
									if(!file.canWrite()) file.createNewFile();
									Files.write(file.toPath(), editor.getText().getBytes());
									
									EditorTab newTab = addTab(file); // add a new tab with newly created/saved file
									Editor newEditor = newTab.getEditorContainer().getEditorPane().getEditor();
									newEditor.setText(Files.readString(file.toPath()));
									
									remove(indexOfComponent(tab)); // remove the old tab which is mapped to temp file that does not exist
								}
							}
						} catch(IOException ex) {
							throw new RuntimeException(ex);
						}
						
						MainWindow.updateState(EditorState.READY);
					}
				}
		);
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
