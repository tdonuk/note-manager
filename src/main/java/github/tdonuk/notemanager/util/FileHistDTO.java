package github.tdonuk.notemanager.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileHistDTO {
	private String tabName;
	private String openedFile;
}
