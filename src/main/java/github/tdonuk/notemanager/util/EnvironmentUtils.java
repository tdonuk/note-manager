package github.tdonuk.notemanager.util;

import github.tdonuk.notemanager.exception.CustomException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
	
	public static Clipboard clipboard() {
		return toolkit.getSystemClipboard();
	}
	
	public static void copyToClipboard() {
		// TODO
	}
	
	public static void beep() {
		toolkit.beep();
	}
	
	public static String osName() {
		return System.getProperty("os.name");
	}
	
	public static String osVersion() {
		return System.getProperty("os.version");
	}
	
	public static String osArch() {
		return System.getProperty("os.arch");
	}
	
	public static String workingDir() {
		return System.getProperty("user.dir");
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
		} else {
			throw new CustomException("os not supported: " + osName);
		}

		return path + File.separator + "NoteManager";
	}

	public static String stateFileDir() {
		return appDataDir() + File.separator + "state.json";
	}
}
