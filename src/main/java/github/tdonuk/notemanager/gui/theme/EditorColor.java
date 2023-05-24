package github.tdonuk.notemanager.gui.theme;

import github.tdonuk.notemanager.constant.Application;
import lombok.AllArgsConstructor;

import java.awt.*;

/**
 * Color object of application. Will have value according to the application's selected theme which are light and dark by default
 */
@AllArgsConstructor
public class EditorColor {
	private Color light;
	private Color dark;
	
	public Color getValue() {
		return Application.THEME == ApplicationTheme.DARK ? dark : light;
	}
}
