package github.tdonuk.notemanager.gui.event.domain;

import github.tdonuk.notemanager.gui.event.constant.EventCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventData {
	protected EventCode operation;
}
