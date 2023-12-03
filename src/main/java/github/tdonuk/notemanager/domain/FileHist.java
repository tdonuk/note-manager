package github.tdonuk.notemanager.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FileHist implements Serializable {
    private String title;
    private String createDate;
    private String lastAccessDate;
    private String openedFile;

    public FileHist(String title, String openedFile) {
        this.title = title;
        this.openedFile = openedFile;

        createDate = LocalDateTime.now().toString();
    }

    @Override
    public String toString() {
        return title + " || " + openedFile;
    }

    @Override
    public int hashCode() {
        return openedFile.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof FileHist otherFileHist)) return false;

        return this.getOpenedFile().compareTo(otherFileHist.getOpenedFile()) == 0;
    }
}
