package github.tdonuk.notemanager.util;

import github.tdonuk.notemanager.exception.CustomException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtils {
    public static boolean writeFile(String path, String content) throws IOException {
        File file = new File(path);

        if (!file.canRead()) file.getParentFile().mkdirs();

        file.createNewFile();

        if (!file.canRead()) throw new CustomException("cannot create file: " + path);

        Files.writeString(file.toPath(), content);

        return true;
    }
}
