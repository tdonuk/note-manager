package github.tdonuk.notemanager.gui;

import github.tdonuk.notemanager.constant.Application;
import github.tdonuk.notemanager.gui.container.Panel;
import github.tdonuk.notemanager.util.EnvironmentUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

@Slf4j
public abstract class AbstractWindow extends JFrame {
	protected abstract JComponent north();
	protected abstract JComponent south();
	protected abstract JComponent east();
	protected abstract JComponent west();
	protected abstract JComponent center();
	
	protected abstract JMenuBar menu();
	protected abstract String title();
	
	protected void initUI() {
		this.setTitle(title());
		this.setSize(3* EnvironmentUtils.screenWidth()/4, 3*EnvironmentUtils.screenHeight()/4);
		this.setLocationRelativeTo(null); // to render the window at the center of the screen
		this.setFont(Application.PRIMARY_FONT);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		Panel mainPanel = new Panel(new BorderLayout()){
			@Override
			public void add(Component comp, Object constraints) {
				if(comp != null) super.add(comp, constraints);
			}
		};
		
		this.setContentPane(mainPanel);
		
		mainPanel.add(north(), BorderLayout.NORTH);
		mainPanel.add(south(), BorderLayout.SOUTH);
		mainPanel.add(east(), BorderLayout.EAST);
		mainPanel.add(west(), BorderLayout.WEST);
		mainPanel.add(center(), BorderLayout.CENTER);
		
		this.setJMenuBar(menu());
		
		addDefaultWindowListener();
	}
	
	private void addDefaultWindowListener() {
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				log.info("window '{}' is opened", title());
				
				afterOpened(e);
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				log.info("Window '{}' is closing", title());
				
				beforeClosing(e);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				log.info("window '{}' is closed", title());
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				log.info("window '{}' is minified", title());
				
				beforeMinimized(e);
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				log.info("window '{}' is maxified", title());
				
				afterMaximized(e);
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				log.info("window '{}' is focused", title());
				
				focusGain(e);
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				log.info("window '{}' has lost focus", title());
				
				focusLost(e);
			}
		});
	}
	
	// Window Events
	
	protected void beforeClosing(WindowEvent e) {
	}
	protected void afterOpened(WindowEvent e) {
	}
	protected void beforeMinimized(WindowEvent e) {
	}
	protected void afterMaximized(WindowEvent e) {
	}
	protected void focusGain(WindowEvent e) {
	}
	protected void focusLost(WindowEvent e) {
	}
}
