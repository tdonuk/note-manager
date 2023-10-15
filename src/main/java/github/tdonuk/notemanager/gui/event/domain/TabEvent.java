package github.tdonuk.notemanager.gui.event.domain;

import github.tdonuk.notemanager.gui.event.constant.EventCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class TabEvent extends EventData{
	private EventCode eventCode;
	private String title;
	private int index;
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;
		
		TabEvent tabEvent = (TabEvent) o;
		
		if(index != tabEvent.index) return false;
		if(eventCode != tabEvent.eventCode) return false;
		return Objects.equals(title, tabEvent.title);
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + eventCode.hashCode();
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + index;
		return result;
	}
}
