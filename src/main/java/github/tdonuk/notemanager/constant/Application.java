package github.tdonuk.notemanager.constant;

import github.tdonuk.notemanager.gui.theme.ApplicationTheme;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Application {
	public static final String VERSION = "1.0.0";
	public static final String NAME = "Note Manager";
	public static final ApplicationTheme THEME = ApplicationTheme.LIGHT;
	public static final Font PRIMARY_FONT = new Font("Courier", Font.PLAIN, 15);
	public static final Font SECONDARY_FONT = new Font("Courier", Font.PLAIN, 10);
}
