package gui.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonEventListeners {
	public static MouseListener mouseClicked(ActionListener listener) {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				listener.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "EMPTY_COMMAND"));
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
			
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			
			}
		};
	}
}
