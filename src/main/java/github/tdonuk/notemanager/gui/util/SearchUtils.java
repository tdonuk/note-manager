package github.tdonuk.notemanager.gui.util;

import github.tdonuk.notemanager.gui.component.Editor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SearchUtils {
	
	public static List<SearchResult> findAll(Editor editor, String searchText) {
		editor.getHighlighter().removeAllHighlights();
		
		if(searchText.isEmpty()) return new ArrayList<>();
		
		Pattern pattern = Pattern.compile(Pattern.quote(searchText), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher matcher = pattern.matcher(editor.getText());
		
		List<SearchResult> results = new ArrayList<>();
		
		while (matcher.find()) {
			results.add(new SearchResult(matcher.start(), matcher.end()));
		}
		
		return results;
	}
	
	public static void markOne(Editor editor, SearchResult target) throws BadLocationException {
		editor.getHighlighter().addHighlight(target.getStart(), target.getEnd(), new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE));
	}
	
}
