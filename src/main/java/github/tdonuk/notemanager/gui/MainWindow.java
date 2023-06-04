package github.tdonuk.notemanager.gui;

import github.tdonuk.notemanager.constant.Application;
import github.tdonuk.notemanager.exception.CustomException;
import github.tdonuk.notemanager.gui.component.Editor;
import github.tdonuk.notemanager.gui.component.EditorTabPane;
import github.tdonuk.notemanager.gui.constant.EditorState;
import github.tdonuk.notemanager.gui.constant.MenuShortcut;
import github.tdonuk.notemanager.gui.container.EditorTab;
import github.tdonuk.notemanager.gui.container.Panel;
import github.tdonuk.notemanager.util.DialogUtils;
import github.tdonuk.notemanager.util.EnvironmentUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Slf4j
public final class MainWindow extends JFrame {
	private static MainWindow instance;
	
	private MainWindow() throws IOException {
		this.setSize(3*EnvironmentUtils.screenWidth()/4, 3*EnvironmentUtils.screenHeight()/4);
		this.setLocationRelativeTo(null); // to create the window at the center of the screen
		this.setTitle(Application.NAME);
		this.setFont(Application.PRIMARY_FONT);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		init();
		
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				log.info("app started");
				
				editorTabs.getTabs().values().stream().findAny().orElseThrow(() -> new RuntimeException("app failed to start")).getEditorContainer().getEditorPane().getEditor().requestFocus(); // open window as focused to editor and ready-to-write
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				log.info("main window is closing");
				StringJoiner stringJoiner = new StringJoiner("\n", "\n", "\n");
				editorTabs.getTabs().values().forEach(tab -> {
					if(tab.hasDiff()) {
						stringJoiner.add(tab.getTitle());
					}
				});
				boolean confirmed = DialogUtils.askConfirmation("Your unsaved changes in these tabs: " + stringJoiner + " will be lost. Do you want to continue?", "Unsaved Changes");
				if(confirmed) {
					e.getWindow().dispose();
					System.exit(0);
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				log.info("main window closed");
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				log.info("app iconified");
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				log.info("app de-iconified");
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				log.info("main window activated");
				try {
					EditorTab tab = editorTabs.getSelectedComponent();
					if(tab != null) tab.reload(false);
				} catch(IOException ex) {
					throw new CustomException(ex);
				}
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				log.info("main window deactivated");
			}
		});
	}
	
	private static final JLabel statusLabel = new JLabel();
	private final JLabel currentPositionLabel = new JLabel();
	private final JLabel totalLinesLabel = new JLabel();
	private final JLabel totalCharactersLabel = new JLabel("");
	
	private JPanel mainPanel;
	
	private EditorTabPane editorTabs;
	
	private void init() throws IOException {
		mainPanel = new Panel(new BorderLayout());
		
		this.setContentPane(mainPanel);
		
		initNorthPanel();
		initWestPanel();
		initEastPanel();
		initSouthPanel();
		initCenterPanel();
		initMenus();
	}
	
	private void initCenterPanel() throws IOException {
  		JPanel centerPanel = new Panel(new BorderLayout());
		centerPanel.setBorder(null);

		editorTabs = new EditorTabPane();
		
		if(editorTabs.getTabs().isEmpty()) {
			createTab("New Document");
		}
		
		centerPanel.add(editorTabs);
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
	}
	
	private void initSouthPanel() {
  		JPanel southPanel = new Panel(new BorderLayout());
		
		southPanel.setBorder(null);
		
		String osInfo = EnvironmentUtils.osName() + " - "+EnvironmentUtils.osArch();
		String versionInfo = "v"+Application.VERSION;
		
		String systemInfo = versionInfo + " ("+osInfo+")";
		
		JLabel systemInfoLabel = new JLabel(systemInfo);
		
		JPanel southWestPanel = new Panel();
		southWestPanel.add(systemInfoLabel);
		
		JPanel southEastPanel = new Panel();
		southEastPanel.add(statusLabel);
		
		southEastPanel.add(currentPositionLabel);
		currentPositionLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
		currentPositionLabel.setToolTipText("Current line | column");
		
		southEastPanel.add(totalLinesLabel);
		totalLinesLabel.setBorder(new EmptyBorder(0, 0, 0, 2));
		
		southEastPanel.add(totalCharactersLabel);
		totalCharactersLabel.setBorder(new EmptyBorder(0, 2, 0, 0));
		
		southPanel.add(southWestPanel, BorderLayout.WEST);
		southPanel.add(southEastPanel, BorderLayout.EAST);
		
		southPanel.setFont(Application.SECONDARY_FONT);
		
		mainPanel.add(southPanel, BorderLayout.SOUTH);
	}
	
	private void initNorthPanel() {
		// TODO: add components to the north side of the main window
	}
	
	private void initWestPanel() {
		// TODO: add components to the west side of the main window
	}
	
	private void initEastPanel() {
		// TODO: add components to the east side of the main window
	}
	
	private void initMenus() {
		JMenuBar topBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File", true);
		JMenu editMenu = new JMenu("Edit", true);
		
		topBar.add(fileMenu);
		topBar.add(editMenu);
		
		initFileMenu(fileMenu);
		initEditMenu(editMenu);
		
		this.setJMenuBar(topBar);
	}
	
	private void initEditMenu(JMenu editMenu) {
		JMenuItem menuItemUndo = new JMenuItem("Undo");
		menuItemUndo.setAccelerator(MenuShortcut.UNDO.getKeyStroke());
		menuItemUndo.addActionListener(a -> editorTabs.getSelectedComponent().getEditorContainer().getEditorPane().getEditor().undo());
		
		JMenuItem menuItemRedo = new JMenuItem("Redo");
		menuItemRedo.setAccelerator(MenuShortcut.REDO.getKeyStroke());
		menuItemRedo.addActionListener(a -> editorTabs.getSelectedComponent().getEditorContainer().getEditorPane().getEditor().redo());
		
		JMenuItem menuItemRefresh = new JMenuItem("Refresh");
		menuItemRefresh.setAccelerator(MenuShortcut.REFRESH.getKeyStroke());
		menuItemRefresh.addActionListener(a -> {
			try {
				editorTabs.getSelectedComponent().reload(true);
			} catch(IOException e) {
				throw new CustomException(e);
			}
		});
		
		JMenuItem menuItemFormat = new JMenuItem("Format");
		menuItemFormat.setAccelerator(MenuShortcut.FORMAT.getKeyStroke());
		menuItemFormat.addActionListener(a -> editorTabs.getSelectedComponent().format());
		
		editMenu.add(menuItemUndo);
		editMenu.add(menuItemRedo);
		editMenu.add(menuItemRefresh);
		editMenu.add(menuItemFormat);
	}
	
	private void initFileMenu(JMenu fileMenu) {
		JMenuItem menuItemNew = new JMenuItem("New");
		menuItemNew.setAccelerator(MenuShortcut.NEW.getKeyStroke());
		menuItemNew.addActionListener(e -> {
			
			try {
				createTab("New Document");
			} catch(IOException ex) {
				throw new CustomException(ex);
			}
		});
		
		JMenuItem menuItemOpen = new JMenuItem("Open");
		menuItemOpen.setAccelerator(MenuShortcut.OPEN.getKeyStroke());
		menuItemOpen.addActionListener(e -> {
			updateState(EditorState.WAITING_INPUT);
			File file = DialogUtils.askFileForOpen();
			
			if(file != null) {
				try {
					createTab(file);
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(this,"Cannot read file: " + ex.getMessage());
				}
			}
			
			updateState(EditorState.READY);
		});
		
		JMenuItem menuItemSave = new JMenuItem("Save");
		menuItemSave.setAccelerator(MenuShortcut.SAVE.getKeyStroke());
		menuItemSave.addActionListener(a -> editorTabs.saveTab(editorTabs.getSelectedComponent()));
		
		fileMenu.add(menuItemNew);
		fileMenu.add(menuItemOpen);
		fileMenu.add(menuItemSave);
	}
	
	public static MainWindow getInstance() throws IOException {
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
		
		totalLinesLabel.setText("lines: " + totalLines);
		totalCharactersLabel.setText("length: " + totalCharacters);
		
		currentPositionLabel.setText(currentLine + " | " + currentColumn);
	}
	
	private EditorTab createTab(File file) throws IOException {
		EditorTab tab = editorTabs.addTab(file);
		
		initTabEditor(tab);
		
		return tab;
	}
	
	private EditorTab createTab(String title) throws IOException {
		EditorTab tab = editorTabs.addTab(title);
		
		initTabEditor(tab);
		
		return tab;
	}
	
	private void initTabEditor(EditorTab tab) {
		Editor editor = tab.getEditorContainer().getEditorPane().getEditor();
		
		editor.addCaretListener(c -> updatePositionInformation(editor));
		addDragListenerToEditor(editor);
		
		editorTabs.setSelectedTab(tab);
	}
	
	private void addDragListenerToEditor(Editor editor) {
		new DropTarget(editor, new DropTargetListener() {
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				log.info("drag entered: " + dtde.getSource());
				updateState(EditorState.WAITING_INPUT);
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
				updateState(EditorState.READY);
			}
			
			@Override
			public void drop(DropTargetDropEvent dtde) {
				java.util.List<DataFlavor> datas = dtde.getCurrentDataFlavorsAsList();
				
				Transferable tr = dtde.getTransferable();
				
				for(DataFlavor data : datas) {
					if(data.isFlavorJavaFileListType()) {
						dtde.acceptDrop(DnDConstants.ACTION_COPY);
						
						try {
							java.util.List<?> list = (List<?>) tr.getTransferData(data);
							
							if(list.size() != 1) {
								dtde.dropComplete(false);
								DialogUtils.showError("Multiple files are not allowed", "Cannot open files");
								return;
							}
							
							File file = (File) list.get(0);
							
							createTab(file);
							
							dtde.dropComplete(true);
						} catch(Exception e) {
							dtde.dropComplete(false);
						}
					}
				}
				updateState(EditorState.READY);
			}
		});
	}
	
}
