package github.tdonuk.notemanager.gui;

import github.tdonuk.notemanager.constant.Application;
import github.tdonuk.notemanager.gui.component.Editor;
import github.tdonuk.notemanager.gui.constant.EditorState;
import github.tdonuk.notemanager.gui.constant.MenuShortcut;
import github.tdonuk.notemanager.gui.container.EditorTab;
import github.tdonuk.notemanager.gui.container.EditorTabPane;
import github.tdonuk.notemanager.gui.container.Panel;
import github.tdonuk.notemanager.util.DialogUtils;
import github.tdonuk.notemanager.util.EnvironmentUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.nio.file.Files;

public final class MainWindow extends JFrame {
	private static MainWindow instance;
	
	private MainWindow() {
		this.setSize(3*EnvironmentUtils.screenWidth()/4, 3*EnvironmentUtils.screenHeight()/4);
		this.setLocationRelativeTo(null); // to create the window at the center of the screen
		this.setTitle(Application.NAME);
		this.setFont(Application.PRIMARY_FONT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		init();
		
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				System.out.println("app started");
				
				editorTabs.getTabs().values().stream().findAny().orElseThrow(() -> new RuntimeException("app failed to start")).getEditorContainer().getEditorPane().getEditor().requestFocus(); // open window as focused to editor and ready-to-write
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("main window is closing");
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println("main window closed");
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				System.out.println("app iconified");
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				System.out.println("app de-iconified");
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				System.out.println("main window activated");
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				System.out.println("main window deactivated");
			}
		});
	}
	
	private static final JLabel statusLabel = new JLabel("");
	
	private JPanel mainPanel;
	
	private EditorTabPane editorTabs;
	
	private void init() {
		mainPanel = new Panel(new BorderLayout());
		
		this.setContentPane(mainPanel);
		
		initNorthPanel();
		initWestPanel();
		initEastPanel();
		initSouthPanel();
		initCenterPanel();
		initMenus();
	}
	
	private void initCenterPanel() {
  		JPanel centerPanel = new Panel(new BorderLayout());
		centerPanel.setBorder(null);

		editorTabs = new EditorTabPane();
		
		centerPanel.add(editorTabs);
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
	}
	
	private void initSouthPanel() {
  		JPanel southPanel = new Panel(new BorderLayout());
		
		southPanel.setBorder(null);
		
		String osInfo = EnvironmentUtils.osName() + " - "+EnvironmentUtils.osArch()+"";
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
  		// JPanel northPanel = new Panel();
	}
	
	private void initWestPanel() {
		// JPanel westPanel = new Panel();
	}
	
	private void initEastPanel() {
  		// JPanel eastPanel = new Panel();
	}
	
	private void initMenus() {
		JMenuBar topMenu = new JMenuBar();
		JMenu fileMenu = new JMenu("File", true);
		
		JMenuItem menuItemNew = new JMenuItem("New");
		menuItemNew.setAccelerator(MenuShortcut.NEW.getKeyStroke());
		menuItemNew.addActionListener(e -> {
			EditorTab tab = editorTabs.addTab("New Document");
			editorTabs.setSelectedTab(tab);
		});
		
		JMenuItem menuItemOpen = new JMenuItem("Open");
		menuItemOpen.setAccelerator(MenuShortcut.OPEN.getKeyStroke());
		menuItemOpen.addActionListener(e -> {
			File file = DialogUtils.askForOpen();
			
			if(file == null) return;
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
		});
		
		topMenu.add(fileMenu);
		
		fileMenu.add(menuItemNew);
		fileMenu.add(menuItemOpen);
		
		this.setJMenuBar(topMenu);
	}
	
	public static MainWindow getInstance() {
		if(instance == null) instance = new MainWindow();
		return instance;
	}
	
	public static void updateState(EditorState state) {
		statusLabel.setText(state.getLabel());
		
		instance.setEnabled(!state.isShouldBlockUi());
	}
	
}
