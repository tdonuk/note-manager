package github.tdonuk.notemanager.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.awt.datatransfer.Clipboard;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnvironmentUtils {
	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	
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
}
