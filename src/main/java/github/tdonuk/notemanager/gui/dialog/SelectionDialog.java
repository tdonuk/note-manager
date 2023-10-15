package github.tdonuk.notemanager.gui.dialog;

import github.tdonuk.notemanager.gui.container.Panel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

@Slf4j
public abstract class SelectionDialog<T> extends UndecoratedDialog {
	protected transient Consumer<T> selectionHandler;
	
	protected SelectionDialog(Point location, Consumer<T> selectionHandler) {
		super(location);
		
		this.selectionHandler = selectionHandler;
		
		JList<T> list = listComponent();
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBorder(null);
		
		Panel listPane = new Panel();
		listPane.setBorder(null);
		listPane.setBackground(null);
		listPane.add(scrollPane);
		
		getContentPane().add(listPane);
		
		setModal(false);
		setAlwaysOnTop(true);
		
		list.registerKeyboardAction(e -> {
			onSelected(list.getSelectedValue());
			dispose();
		}, "selectItem", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		list.registerKeyboardAction(e -> dispose(), "cancelSelection", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		list.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()== 2 && e.getButton() == MouseEvent.BUTTON1) {
					onSelected(list.getSelectedValue());
					dispose();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// not necessary
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// not necessary
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// not necessary
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// not necessary
			}
		});
		
		list.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				log.info("dialog gained focus");
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				log.info("dialog lost focus");
				dispose();
			}
		});
	}
	
	protected final void onSelected(T value) {
		MDC.put("elementClass", value.getClass().getName());
		log.info("user selected element: " + value);
		selectionHandler.accept(value);
		MDC.clear();
	}
	
	protected abstract JList<T> listComponent();
}
