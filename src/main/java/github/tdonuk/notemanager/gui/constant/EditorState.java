package github.tdonuk.notemanager.gui.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EditorState {
	READY("Ready", false),
	SAVING("Saving", true),
	READING_CONTENT("Reading", true),
	WRITING_CONTENT("Writing", true);
	
	private final String label;
	private final boolean shouldBlockUi;
	
}
