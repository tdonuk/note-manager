package github.tdonuk.notemanager.util;

import lombok.Data;

import java.util.List;

@Data
public class StateDTO {
	private List<FileHistDTO> openedFiles;
	private String leaveDate;
}
