package github.tdonuk.notemanager.util;

import github.tdonuk.notemanager.exception.CustomException;
import github.tdonuk.notemanager.gui.window.MainWindow;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class EnvironmentUtils {
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();

    public static Dimension screenSize() {
        return toolkit.getScreenSize();
    }

    public static int screenWidth() {
        return screenSize().width;
    }

    public static int screenHeight() {
        return screenSize().height;
    }

    public static void beep() {
        toolkit.beep();
    }

    public static String osName() {
        return System.getProperty("os.name");
    }

    public static String userDir() {
        return System.getProperty("user.home");
    }

    public static String appDataDir() {
        String path;
        String osName = osName();
        if (osName.startsWith("Windows")) {
            String appDataDir = System.getenv("LOCALAPPDATA");

            path = StringUtils.isBlank(appDataDir) ? userDir() : appDataDir;
        } else if (osName.startsWith("Linux")) {
            String xdgDataHome = System.getenv("XDG_DATA_HOME");

            path = StringUtils.isBlank(xdgDataHome) ? userDir() + File.separator + ".local" + File.separator + "share" : xdgDataHome;
        } else if (osName.startsWith("Mac OS X")) {

            path = userDir() + File.separator + "Library" + File.separator + "Application Support";
        } else {
            throw new CustomException("os not supported: " + osName);
        }

        return path + File.separator + "NoteManager";
    }

    public static String stateFileDir() {
        return appDataDir() + File.separator + "state.json";
    }

    public static String tempFilesDir() {
        return appDataDir() + File.separator + "temp";
    }

    public static void shutdown() {
        log.info("application shutdown");
        MainWindow.getInstance().dispose();
        System.exit(-1);
    }
}
