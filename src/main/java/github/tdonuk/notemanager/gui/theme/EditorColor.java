package gui.theme;

import constant.Application;
import gui.theme.ApplicationTheme;
import lombok.AllArgsConstructor;

import java.awt.*;

@AllArgsConstructor
public class EditorColor {
	private Color light;
	private Color dark;
	
	public Color getValue() {
		return Application.THEME == ApplicationTheme.DARK ? dark : light;
	}
}
