package github.tdonuk.notemanager.gui.util;

import github.tdonuk.notemanager.exception.CustomException;
import github.tdonuk.notemanager.gui.component.Editor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SearchUtils {
	public static synchronized List<SearchResult> markAll(Editor editor, String searchText) {
		editor.getHighlighter().removeAllHighlights();
		
		if(searchText.isEmpty()) return new ArrayList<>();
		
		Pattern pattern = Pattern.compile(Pattern.quote(searchText), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher matcher = pattern.matcher(editor.getText());
		
		List<SearchResult> results = new ArrayList<>();
		
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			
			results.add(new SearchResult(start, end));
			
			try {
				editor.getHighlighter().addHighlight(start, end, DefaultHighlighter.DefaultPainter);
			} catch(BadLocationException e) {
				throw new CustomException(e);
			}
		}
		
		return results;
	}
	
}
