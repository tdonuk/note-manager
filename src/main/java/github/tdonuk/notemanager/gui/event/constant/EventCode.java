package github.tdonuk.notemanager.gui.event.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventCode {
	ADD_TAB("addTab"), REMOVE_TAB("removeTab");
	
	private final String code;
}
