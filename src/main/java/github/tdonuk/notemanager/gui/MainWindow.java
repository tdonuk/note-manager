package github.tdonuk.notemanager.gui;

import github.tdonuk.notemanager.constant.Application;
import github.tdonuk.notemanager.gui.component.Editor;
import github.tdonuk.notemanager.gui.component.SecondaryLabel;
import github.tdonuk.notemanager.gui.container.EditorTab;
import github.tdonuk.notemanager.gui.container.EditorTabPane;
import github.tdonuk.notemanager.gui.container.PrimaryPanel;
import github.tdonuk.notemanager.gui.container.SecondaryPanel;
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
				
				editorTabs.getTabs().get(0).getEditorContainer().getEditorPane().getEditor().requestFocus(); // open window as focused to editor and ready-to-write
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
	
	private JMenuBar menuBar;
	
	private JMenu fileMenu;
	
	private JMenuItem menuItemNew, menuItemOpen;
	
	private JPanel mainPanel, eastPanel, westPanel, northPanel, southPanel, centerPanel;
	
	private EditorTabPane editorTabs;
	
	private void init() {
		mainPanel = new PrimaryPanel(new BorderLayout());
		
		this.setContentPane(mainPanel);
		
		initNorthPanel();
		initWestPanel();
		initEastPanel();
		initSouthPanel();
		initCenterPanel();
		initMenus();
	}
	
	private void initCenterPanel() {
		centerPanel = new SecondaryPanel(new BorderLayout());
		centerPanel.setBorder(null);

		editorTabs = new EditorTabPane();
		
		centerPanel.add(editorTabs);
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
	}
	
	private void initSouthPanel() {
		southPanel = new PrimaryPanel(new BorderLayout());
		southPanel.setBorder(null);
		
		southPanel.add(new SecondaryLabel("v"+Application.VERSION), BorderLayout.WEST);
		southPanel.add(new SecondaryLabel(EnvironmentUtils.osName() + " ("+EnvironmentUtils.osArch()+")"), BorderLayout.EAST);
		
		mainPanel.add(southPanel, BorderLayout.SOUTH);
	}
	
	private void initNorthPanel() {
		northPanel = new PrimaryPanel();
	}
	
	private void initWestPanel() {
		westPanel = new PrimaryPanel();
	}
	
	private void initEastPanel() {
		eastPanel = new PrimaryPanel();
	}
	
	private void initMenus() {
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File", true);
		
		menuItemNew = new JMenuItem("New");
		menuItemNew.addActionListener(e -> {
			EditorTab tab = editorTabs.addTab("New Document");
			editorTabs.setSelectedTab(tab);
		});
		
		menuItemOpen = new JMenuItem("Open");
		menuItemOpen.addActionListener(e -> {
			File file = DialogUtils.askForFile();
			
			if(file == null) return;
			byte[] content;
			try {
				content = Files.readAllBytes(file.toPath());
				
				EditorTab tab = editorTabs.addTab(file.getName());
				
				Editor editor = tab.getEditorContainer().getEditorPane().getEditor();
				
				String contentStr = new String(content);
				
				editor.setText(contentStr);
				editorTabs.setSelectedTab(tab);
				
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(this,"Cannot read file: " + ex.getMessage());
			}
		});
		
		menuBar.add(fileMenu);
		
		fileMenu.add(menuItemNew);
		fileMenu.add(menuItemOpen);
		
		this.setJMenuBar(menuBar);
	}
	
	public static MainWindow getInstance() {
		if(instance == null) instance = new MainWindow();
		return instance;
	}
	
}
