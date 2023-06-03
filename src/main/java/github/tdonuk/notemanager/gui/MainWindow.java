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
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public final class MainWindow extends JFrame {
	private static MainWindow instance;
	
	private MainWindow() throws IOException {
		this.setSize(3*EnvironmentUtils.screenWidth()/4, 3*EnvironmentUtils.screenHeight()/4);
		this.setLocationRelativeTo(null); // to create the window at the center of the screen
		this.setTitle(Application.NAME);
		this.setFont(Application.PRIMARY_FONT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
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
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				log.info("main window deactivated");
			}
		});
	}
	
	private static final JLabel statusLabel = new JLabel("");
	
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
		southWestPanel.setFont(Application.SECONDARY_FONT);
		
		southPanel.add(southWestPanel, BorderLayout.WEST);
		southPanel.add(statusLabel, BorderLayout.EAST);
		
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
				editorTabs.getSelectedComponent().reload();
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
				EditorTab tab = editorTabs.addTab("New Document");
				editorTabs.setSelectedTab(tab);
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
				byte[] content;
				try {
					content = Files.readAllBytes(file.toPath());
					
					EditorTab tab = editorTabs.addTab(file);
					
					Editor editor = tab.getEditorContainer().getEditorPane().getEditor();
					
					String contentStr = new String(content);
					
					editor.setText(contentStr);
					editorTabs.setSelectedTab(tab);
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
	
}
