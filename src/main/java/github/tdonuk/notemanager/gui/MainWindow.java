package gui;

import constant.Application;
import gui.component.Editor;
import gui.component.SecondaryButton;
import gui.component.SecondaryLabel;
import gui.container.EditorTab;
import gui.container.EditorTabPane;
import gui.container.PrimaryPanel;
import gui.container.SecondaryPanel;
import util.DialogUtils;
import util.EnvironmentUtils;
import util.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MainWindow extends JFrame {
	private static MainWindow instance;
	
	private MainWindow() {
		init();
	}
	
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem menuItemNew, menuItemOpen;
	
	private JPanel mainPanel, eastPanel, westPanel, northPanel, southPanel, centerPanel;
	
	private EditorTabPane editorTabs;
	
	private void init() {
		this.setSize(3*EnvironmentUtils.screenWidth()/4, 3*EnvironmentUtils.screenHeight()/4);
		this.setLocationRelativeTo(null);
		this.setTitle(Application.NAME);
		this.setFont(Application.PRIMARY_FONT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
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
		
		// top level containers
		editorTabs = new EditorTabPane();
		
		centerPanel.add(editorTabs);
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		editorTabs.getTabs().get(0).getEditorContainer().getEditorPane().getEditor().requestFocus();
	}
	
	private void initSouthPanel() {
		southPanel = new PrimaryPanel();
		
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
			editorTabs.addTab("New Document");
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
