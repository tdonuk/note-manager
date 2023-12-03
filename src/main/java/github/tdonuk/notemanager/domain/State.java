package github.tdonuk.notemanager.domain;

import lombok.Data;

import java.util.List;

@Data
public class State {
    private List<FileHist> history;
    private List<FileHist> currentTabs;
    private FileHist selectedTab;
    private Integer caretPosition;
    private Integer scrollValue;
    private String leaveDate;
}
