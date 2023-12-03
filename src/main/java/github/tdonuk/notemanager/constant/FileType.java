package github.tdonuk.notemanager.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum FileType {
    XML(".xml"), HTML(".html"), TXT(".txt"), JSON(".json"), XSL(".xsl"), XSLT(".xslt");

    private final String extension;

    public static FileType findByExtension(String extension) {
        try {
            return Arrays.stream(values()).filter(v -> v.getExtension().equalsIgnoreCase(extension)).findAny().orElse(TXT);
        } catch (Exception e) {
            return TXT;
        }
    }
}
