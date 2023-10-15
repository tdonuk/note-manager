package github.tdonuk.notemanager.gui.component;

import github.tdonuk.notemanager.constant.FileType;
import github.tdonuk.notemanager.domain.FileHist;
import github.tdonuk.notemanager.exception.CustomException;
import github.tdonuk.notemanager.gui.constant.EditorState;
import github.tdonuk.notemanager.gui.container.EditorTab;
import github.tdonuk.notemanager.gui.event.constant.EventCode;
import github.tdonuk.notemanager.gui.event.domain.TabEvent;
import github.tdonuk.notemanager.gui.event.listener.TabListener;
import github.tdonuk.notemanager.gui.window.MainWindow;
import github.tdonuk.notemanager.util.DialogUtils;
import github.tdonuk.notemanager.util.EnvironmentUtils;
import github.tdonuk.notemanager.util.HistoryCache;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.*;
import java.util.function.Consumer;

@Getter
@Slf4j
public class EditorTabManager extends JTabbedPane implements TabManager<EditorTab, FileHist> {
	private final Map<FileHist, EditorTab> tabs = new HashMap<>();
	
	private final List<TabListener> eventListeners = new ArrayList<>();
	
	private final transient Timer headerUpdater = new Timer("headerUpdater");
	
	public EditorTabManager(Consumer<EditorTab> selectionHandler) {
		super();
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		addDefaultListeners(selectionHandler);
		
		headerUpdater.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				log.debug("checking for diffs");
				revalidateTabComponents();
			}
		}, 0, 1500);
	}
	
	@Override
	public EditorTab addTab(FileHist file, boolean isTemporary) throws IOException {
		if(isTemporary &&  file == null) {
			File tempFile = new File(EnvironmentUtils.tempFilesDir(), makeTitleUnique("New Document", FileType.TXT.getExtension()));
			
			file = new FileHist(tempFile.getName(), tempFile.getAbsolutePath());
		}
		
		if(tabs.containsKey(file)) { // user opens a file that is already opened in another tab
			setSelectedTab(tabs.get(file));
			return tabs.get(file);
		}
		
		File openedFile = new File(file.getOpenedFile());
		String title = file.getTitle();
		
		EditorTab tabToAdd = new EditorTab(file, isTemporary);
		
		Editor editor = tabToAdd.getEditorContainer().getEditorPane().getEditor();
		
		if(existsWithFileName(openedFile.getName())) tabToAdd.setDuplicated(true);
		
		addDefaultDragListener(editor);
		
		this.add(title, tabToAdd);
		
		for(TabListener tabListener : eventListeners) {
			tabListener.tabAdded(new TabEvent(EventCode.ADD_TAB, title, indexOfTab(title)));
		}
		
		return tabToAdd;
	}
	
	@Override
	public void setTitleAt(int index, String title) { // updates the tab header component (the small components at the top of the tabbed pane container that has the tab titles in it)
		setTabComponentAt(index, ((EditorTab) getComponentAt(index)).getHeader());
	}
	
	/**
	 * Never use this directly. this should be called via {@link #addTab(FileHist, boolean)}
	 */
	@Override
	public Component add(String title, Component component) {
		EditorTab tab = (EditorTab) component;
		
		Component added = super.add(title, tab);
		tabs.put(tab.getFileHist(), tab);
		
		setTabComponentAt(indexOfComponent(added), ((EditorTab) added).getHeader());
		
		return added;
	}
	
	@Override
	public void remove(int index) {
		EditorTab toDelete = (EditorTab) getComponentAt(index);
		
		if(toDelete.hasDiff()) {
			boolean accepted = DialogUtils.askApprovation("Your unsaved changes will be lost. Still close this tab?", "Unsaved Changes");
			if(!accepted) return;
		}
		
		super.remove(index);
		
		tabs.values().remove(toDelete);
		
		revalidateTabComponents();
	}
	
	private void revalidateTabComponents() {
		// this keeps index part of the tab name updated
		// example: there is two tabs with names like "New Document.txt" and "New Document.txt (1)"
		// when user closes "New Document.txt", other tab now does not need to indicate its index with "(1)"
		// so we should update its tab title with the same name of its opened file
		for(EditorTab tab : tabs.values()) {
			int tabCountWithThisName = getTabsWithFileName(new File(tab.getFileHist().getOpenedFile()).getName()).size();
			tab.setDuplicated(tabCountWithThisName > 1);
			
			if(indexOfTab(tab.getTitle()) != -1) setTabComponentAt(indexOfTab(tab.getTitle()), tab.getHeader());
		}
	}
	
	@Override
	public EditorTab getSelectedComponent() {
		return (EditorTab) super.getSelectedComponent();
	}
	
	
	@Override
	public EditorTab getTab(FileHist file) {
		return tabs.get(file);
	}
	
	public List<EditorTab> getTabsWithFileName(String fileName) {
		return tabs.values().stream().filter(tab -> new File(tab.getFileHist().getOpenedFile()).getName().equals(fileName)).toList();
	}
	
	@Override
	public void setSelectedTab(EditorTab tab) {
		this.setSelectedIndex(indexOfComponent(tab));
	}
	
	private String makeTitleUnique(String title, String extension) {
		String titleWithIndex = title + extension;
		int index = 0;
		
		while(existsWithFileName(titleWithIndex)) {
			titleWithIndex = title + " (" + (++index) + ")" + extension;
		}
		
		return titleWithIndex;
	}
	
	public boolean existsWithFileName(String fileName) {
		return tabs.values().stream().anyMatch(tab -> new File(tab.getFileHist().getOpenedFile()).getName().equals(fileName));
	}
	
	public void saveTab(EditorTab tab) {
		if(!tab.hasDiff()) return;
		
		log.info("saving " + tab.getFileHist().getOpenedFile());
		
		MainWindow.updateState(EditorState.SAVING);
		
		try {
			FileHist savedFile = tab.save();
			
			if(savedFile != null) {
				reloadTab(tab);
				
				log.info("saved to: " + savedFile.getOpenedFile());
			}
			
			revalidateTabComponents();
		} catch(IOException ex) {
			throw new CustomException(ex);
		}
		
		MainWindow.updateState(EditorState.READY);
	}
	
	public void reloadTab(EditorTab tab) throws IOException {
		log.info("reloading tab: " + tab.getTitle());
		tab.reload(false);
	}
	
	public void addDefaultDragListener(Editor editor) {
		new DropTarget(editor, new DropTargetListener() {
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				log.info("drag entered: " + dtde.getSource());
			}
			
			@Override
			public void dragOver(DropTargetDragEvent dtde) {
				// prints too much logs
			}
			
			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {
				log.info("drop changed: " + dtde.getSource());
			}
			
			@Override
			public void dragExit(DropTargetEvent dte) {
				log.info("drag exit: " + dte.getSource());
			}
			
			@Override
			public void drop(DropTargetDropEvent dtde) {
				List<DataFlavor> droppedDataList = dtde.getCurrentDataFlavorsAsList();
				
				Transferable tr = dtde.getTransferable();
				
				for(DataFlavor data : droppedDataList) {
					if(data.isFlavorJavaFileListType()) {
						dtde.acceptDrop(DnDConstants.ACTION_COPY);
						
						try {
							List<?> list = (List<?>) tr.getTransferData(data);
							
							if(list.size() != 1) {
								dtde.dropComplete(false);
								DialogUtils.showError("Multiple files are not allowed", "Cannot open files");
								return;
							}
							
							File file = (File) list.get(0);
							
							FileHist fileHist = HistoryCache.getByFile(file);
							
							if(fileHist == null) fileHist = new FileHist(file.getName(), file.getAbsolutePath());
							
							addTab(fileHist, false);
							
							dtde.dropComplete(true);
						} catch(Exception e) {
							dtde.dropComplete(false);
						}
					}
				}
			}
		});
	}
	
	@Override
	public void addTabListener(TabListener eventListener) {
		this.eventListeners.add(eventListener);
	}
	
	@Override
	public void removeTabListener(TabListener tabListener) {
		this.eventListeners.remove(tabListener);
	}
	
	@Override
	public void clearTabListeners() {
		this.eventListeners.clear();
	}
	
	private void addDefaultListeners(Consumer<EditorTab> selectionHandler) {
		this.addChangeListener(e -> {
			EditorTabManager tabbedPane = (EditorTabManager) e.getSource();
			
			EditorTab selectedTab = tabbedPane.getSelectedComponent();
			
			if(selectedTab != null) {
				log.info("selected tab: " + selectedTab.getTitle());
				
				selectedTab.getFileHist().setLastAccessDate(LocalDateTime.now().toString());
				
				try {
					reloadTab(selectedTab);
				} catch(IOException ex) {
					throw new CustomException(ex);
				}
			}
			
			selectionHandler.accept(selectedTab);
		});
		
		this.addTabListener(new TabListener() {
			@Override
			public void tabAdded(TabEvent e) {
				EditorTab tab = (EditorTab) getComponentAt(indexOfTab(e.getTitle()));
				tab.getFileHist().setLastAccessDate(LocalDateTime.now().toString());
				HistoryCache.addIfAbsent(tab.getFileHist());
			}
			
			@Override
			public void tabRemoved(TabEvent e) {
				// do some business
			}
		});
	}
}
