package github.tdonuk.notemanager.gui.window;

import github.tdonuk.notemanager.constant.Application;
import github.tdonuk.notemanager.domain.FileHist;
import github.tdonuk.notemanager.domain.State;
import github.tdonuk.notemanager.exception.CustomException;
import github.tdonuk.notemanager.gui.AbstractWindow;
import github.tdonuk.notemanager.gui.component.Editor;
import github.tdonuk.notemanager.gui.component.EditorScrollPane;
import github.tdonuk.notemanager.gui.component.EditorTabManager;
import github.tdonuk.notemanager.gui.constant.EditorState;
import github.tdonuk.notemanager.gui.constant.MenuShortcut;
import github.tdonuk.notemanager.gui.container.EditorTab;
import github.tdonuk.notemanager.gui.container.Panel;
import github.tdonuk.notemanager.gui.dialog.HistoryDialog;
import github.tdonuk.notemanager.gui.fragment.SearchPanel;
import github.tdonuk.notemanager.util.*;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

@Slf4j
public final class MainWindow extends AbstractWindow {
	private static MainWindow instance;

	private static final JLabel statusLabel = new JLabel("0");
	private final JLabel currentPositionLabel = new JLabel("Ln:0 Col:0");
	private final JLabel totalLinesLabel = new JLabel("0");
	private final JLabel totalCharactersLabel = new JLabel("0");
	private final JLabel selectedTextLabel = new JLabel("0");
	
	private final JLabel currentFileLabel = new JLabel();
	
	private SearchPanel searchBar;
	private JTextField searchField;
	
	private final JProgressBar progressBar = new JProgressBar();
	
	private EditorTabManager tabManager;
	
	public MainWindow() {
		initUI();
	}
	
	@Override
	protected String title() {
		return Application.NAME;
	}
	
	@Override
	protected JComponent north() {
		Runnable doRemoveAllMarks = () -> {
			Editor editor = tabManager.getSelectedComponent().getEditorContainer().getEditorPane().getEditor();
			editor.getHighlighter().removeAllHighlights();
		};
		
		Runnable doSearch = () -> {
			SearchWorker searchWorker = new SearchWorker(tabManager.getSelectedComponent().getEditorContainer().getEditorPane().getEditor(), searchField.getText(), progressBar);
			
			searchWorker.execute();
		};
		
		searchBar = new SearchPanel(doRemoveAllMarks, doSearch);
		
		searchBar.setVisible(false);
		
		this.searchField = searchBar.getSearchInput();
		
		return searchBar;
	}
	
	@Override
	protected JComponent south() {
		JPanel southPanel = new Panel(new BorderLayout());
		
		southPanel.setMaximumSize(new Dimension(getWidth(), 1));
		
		southPanel.setBorder(new LineBorder(Color.gray, 1));
		
		JPanel southWestPanel = new Panel();
		southWestPanel.add(currentFileLabel);
		
		JPanel southEastPanel = new Panel();
		
		JPanel progressBarPanel = new Panel();
		progressBarPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		progressBarPanel.setSize(progressBarPanel.getWidth(), 20);
		southEastPanel.add(progressBarPanel);
		
		progressBar.setVisible(false);
		progressBar.setForeground(Color.black);
		progressBar.setBorderPainted(false);
		progressBarPanel.add(progressBar);
		
		southEastPanel.add(statusLabel);
		statusLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
		
		southEastPanel.add(selectedTextLabel);
		selectedTextLabel.setBorder(new EmptyBorder(0, 5, 0, 5));

		southEastPanel.add(currentPositionLabel);
		currentPositionLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
		currentPositionLabel.setToolTipText("Current position");
		
		southEastPanel.add(totalLinesLabel);
		totalLinesLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
		
		southEastPanel.add(totalCharactersLabel);
		totalCharactersLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
		
		southPanel.add(southWestPanel, BorderLayout.WEST);
		southPanel.add(southEastPanel, BorderLayout.EAST);
		
		southPanel.setFont(Application.SECONDARY_FONT);
		
		return southPanel;
	}
	
	@Override
	protected JComponent east() {
		return null;
	}
	
	@Override
	protected JComponent west() {
		return null;
	}
	
	@Override
	protected JComponent center() {
		JPanel centerPanel = new Panel(new BorderLayout());
		centerPanel.setBorder(null);
		
		Consumer<EditorTab> tabSelectionHandler = editorTab -> {
			if(editorTab != null) currentFileLabel.setText(editorTab.getFileHist().getOpenedFile());
		};
		
		tabManager = new EditorTabManager(tabSelectionHandler);
		
		centerPanel.add(tabManager);
		
		return centerPanel;
	}
	
	@Override
	protected JMenuBar menu() {
		JMenuBar topBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File", true);
		JMenu editMenu = new JMenu("Edit", true);
		
		topBar.add(fileMenu);
		topBar.add(editMenu);
		
		initFileMenu(fileMenu);
		initEditMenu(editMenu);
		
		return topBar;
	}
	
	@Override
	protected void beforeClosing(WindowEvent e) {
		StringJoiner stringJoiner = new StringJoiner("\n", "\n", "\n");
		stringJoiner.setEmptyValue("");
		tabManager.getTabs().values().forEach(tab -> {
			if(tab.hasDiff()) {
				stringJoiner.add(tab.getTitle());
			}
		});
		
		boolean exitFlag = true;
		if(stringJoiner.length() != 0) {
			exitFlag = DialogUtils.askApprovation(stringJoiner + "\nYour unsaved changes in above tabs will be lost. Do you want to continue?", "Unsaved Changes");
		}
		
		if(exitFlag) {
			try {
				if(tabManager.getTabs().isEmpty()) {
					Files.deleteIfExists(Paths.get(EnvironmentUtils.stateFileDir()));
					
					EnvironmentUtils.shutdown();
				}
				
				EditorTab selectedTab = tabManager.getSelectedComponent();
				
				State state = new State();
				List<FileHist> fileHists = tabManager.getTabs().values().stream().map(EditorTab::getFileHist).toList();
				state.setCurrentTabs(fileHists);
				state.setSelectedTab(tabManager.getSelectedComponent().getFileHist());
				state.setHistory(HistoryCache.get().stream().sorted(Comparator.comparing(FileHist::getLastAccessDate).reversed()).limit(30).toList());
				state.setCaretPosition(selectedTab.getEditorContainer().getEditorPane().getEditor().getCaretPosition());
				state.setScrollValue(selectedTab.getEditorContainer().getEditorPane().getVerticalScrollBar().getValue());
				state.setLeaveDate(LocalDateTime.now().toString());

				FileUtils.writeFile(EnvironmentUtils.stateFileDir(), StringUtils.stringifyToJSON(state));
				
				EnvironmentUtils.shutdown();
			} catch(Exception exc) {
				log.error("exception happened while saving the app state: " + exc.getMessage(), exc);
				EnvironmentUtils.shutdown();
			}
		}
	}
	
	@Override
	protected void afterOpened(WindowEvent e) {
		if(!tabManager.getTabs().isEmpty()) {
			tabManager.getSelectedComponent().getEditorContainer().getEditorPane().getEditor().requestFocus(); // open window as focused to editor and ready-to-write
		}
	}
	
	@Override
	protected void afterMaximized(WindowEvent e) {
		try {
			EditorTab tab = tabManager.getSelectedComponent();
			if(tab != null) tab.reload(false);
		} catch(IOException ex) {
			throw new CustomException(ex);
		}
	}
	
	private void initEditMenu(JMenu editMenu) {
		JMenuItem menuItemUndo = new JMenuItem("Undo");
		menuItemUndo.setAccelerator(MenuShortcut.UNDO.getKeyStroke());
		menuItemUndo.addActionListener(a -> tabManager.getSelectedComponent().getEditorContainer().getEditorPane().getEditor().undo());
		
		JMenuItem menuItemRedo = new JMenuItem("Redo");
		menuItemRedo.setAccelerator(MenuShortcut.REDO.getKeyStroke());
		menuItemRedo.addActionListener(a -> tabManager.getSelectedComponent().getEditorContainer().getEditorPane().getEditor().redo());
		
		JMenuItem menuItemRefresh = new JMenuItem("Refresh");
		menuItemRefresh.setAccelerator(MenuShortcut.REFRESH.getKeyStroke());
		menuItemRefresh.addActionListener(a -> {
			try {
				tabManager.getSelectedComponent().reload(true);
			} catch(IOException e) {
				throw new CustomException(e);
			}
		});
		
		JMenuItem menuItemSearch = new JMenuItem("Search");
		menuItemSearch.setAccelerator(MenuShortcut.SEARCH.getKeyStroke());
		menuItemSearch.addActionListener(a -> {
			searchBar.setVisible(true);
			searchField.requestFocus();
			searchField.selectAll();
		});
		
		JMenuItem menuItemFormat = new JMenuItem("Format");
		menuItemFormat.setAccelerator(MenuShortcut.FORMAT.getKeyStroke());
		menuItemFormat.addActionListener(a -> tabManager.getSelectedComponent().format());
		
		editMenu.add(menuItemUndo);
		editMenu.add(menuItemRedo);
		editMenu.add(menuItemRefresh);
		editMenu.add(menuItemFormat);
		editMenu.add(menuItemSearch);
	}
	
	private void initFileMenu(JMenu fileMenu) {
		JMenuItem menuItemNew = new JMenuItem("New");
		menuItemNew.setAccelerator(MenuShortcut.NEW.getKeyStroke());
		menuItemNew.addActionListener(e -> {
			
			try {
				createTab(null, true);
			} catch(IOException ex) {
				throw new CustomException(ex);
			}
		});
		
		JMenuItem menuItemOpen = new JMenuItem("Open");
		menuItemOpen.setAccelerator(MenuShortcut.OPEN.getKeyStroke());
		menuItemOpen.addActionListener(e -> {
			updateState(EditorState.WAITING);
			File file = DialogUtils.askFileForOpen();
			
			if(file != null) {
				try {
					FileHist fileHist = HistoryCache.getByFile(file);
					if(fileHist == null) fileHist = new FileHist(file.getName(), file.getAbsolutePath());
					
					createTab(fileHist, false);
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(this,"Cannot read file: " + ex.getMessage());
				}
			}
			
			updateState(EditorState.READY);
		});
		
		JMenuItem menuItemHistory = new JMenuItem("History");
		menuItemHistory.setAccelerator(MenuShortcut.HISTORY.getKeyStroke());
		menuItemHistory.addActionListener(e -> {
			log.info("opening history dialog");
			requestFocus();
			
			HistoryDialog historyDialog = new HistoryDialog(MouseInfo.getPointerInfo().getLocation(), fileHist -> {
				if(tabManager.getTabs().get(fileHist) != null) {
					tabManager.setSelectedTab(tabManager.getTabs().get(fileHist));
				} else {
					try {
						createTab(fileHist, false);
					} catch(IOException ex) {
						throw new CustomException(ex);
					}
				}
			});
			historyDialog.requestFocus();
			
			historyDialog.setVisible(true);
		});
		
		JMenuItem menuItemSave = new JMenuItem("Save");
		menuItemSave.setAccelerator(MenuShortcut.SAVE.getKeyStroke());
		menuItemSave.addActionListener(a -> tabManager.saveTab(tabManager.getSelectedComponent()));
		
		fileMenu.add(menuItemNew);
		fileMenu.add(menuItemOpen);
		fileMenu.add(menuItemSave);
		fileMenu.add(menuItemHistory);
	}
	
	public static MainWindow getInstance() {
		if(instance == null) instance = new MainWindow();
		return instance;
	}
	
	public static void updateState(EditorState state) {
		statusLabel.setText(state.getLabel());
		
		instance.setEnabled(!state.isShouldBlockUi());
	}
	
	private void updatePositionInformation(Editor editor) {
		int totalLines = editor.getLineCount();
		int totalCharacters = editor.getText().length();
		int currentLine = editor.getCaretLineNumber() + 1;
		int currentColumn = editor.getCaretOffsetFromLineStart();
		int selectedTextSize = StringUtils.length(editor.getSelectedText());

		totalLinesLabel.setText("lines: " + totalLines);
		totalCharactersLabel.setText("length: " + totalCharacters);
		currentPositionLabel.setText("Ln:" + currentLine + " Col:" + currentColumn);
		selectedTextLabel.setText("Sel: "+selectedTextSize);
	}
	
	private EditorTab createTab(FileHist file, boolean isTemporary) throws IOException {
		EditorTab tab = tabManager.addTab(file, isTemporary);
		
		Editor editor = tab.getEditorContainer().getEditorPane().getEditor();
		
		editor.addCaretListener(c -> updatePositionInformation(editor));
		
		tabManager.setSelectedTab(tab);
		
		return tab;
	}

	public void initializeWithState(State state) throws IOException {
		for(FileHist fileHist: state.getCurrentTabs()) {
			createTab(fileHist, false);
		}
		
		for(EditorTab tab : tabManager.getTabs().values()) {
			if(state.getSelectedTab().getOpenedFile().equals(tab.getFileHist().getOpenedFile())) {
				tabManager.setSelectedComponent(tab);
				
				EditorScrollPane scrollPane = tabManager.getSelectedComponent().getEditorContainer().getEditorPane();
				
				scrollPane.getEditor().setCaretPosition(state.getCaretPosition());
				scrollPane.getViewport().setViewPosition(new Point(0, state.getScrollValue()));
				
				scrollPane.getEditor().requestFocus();
			}
		}
		
		tabManager.getSelectedComponent().getEditorContainer().getEditorPane().getEditor().requestFocus();
	}

	public void initializeWithoutState() {
		// add an empty  tab if it is required
	}
	
}
