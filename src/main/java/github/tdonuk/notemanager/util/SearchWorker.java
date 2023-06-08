package github.tdonuk.notemanager.util;

import github.tdonuk.notemanager.exception.CustomException;
import github.tdonuk.notemanager.gui.MainWindow;
import github.tdonuk.notemanager.gui.component.Editor;
import github.tdonuk.notemanager.gui.constant.EditorState;
import github.tdonuk.notemanager.gui.util.SearchResult;
import github.tdonuk.notemanager.gui.util.SearchUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SearchWorker extends SwingWorker<Void, ProgressDTO> {
	private final Editor editor;
	private final String searchText;
	private final JProgressBar progress;
	
	public SearchWorker(Editor editor, String searchText, JProgressBar progress) {
		this.editor = editor;
		this.searchText = searchText;
		this.progress = progress;
	}
	
	@Override
	protected Void doInBackground() {
		MainWindow.updateState(EditorState.WAITING);
		
		List<SearchResult> results = SearchUtils.findAll(editor, searchText);
		
		if(progress != null) {
			progress.setVisible(true);
			progress.setMaximum(results.size()-1);
			progress.setMinimum(0);
			progress.setStringPainted(true);
			progress.setForeground(Color.black);
		}
		
		for (SearchResult r : results) {
			if (isCancelled()) {
				break;
			}
			
			process(List.of(new ProgressDTO(0,0, results.indexOf(r))));
			
			new Thread(() -> {
				try {
					SearchUtils.markOne(editor, r);
				} catch(BadLocationException e) {
					throw new CustomException(e);
				}
			}).start();
		}
		
		MainWindow.updateState(EditorState.READY);
		return null;
	}
	
	@Override
	protected void process(List<ProgressDTO> chunks) {
		ProgressDTO value = chunks.get(chunks.size() - 1);
		if(progress != null) {
			progress.setValue(value.getCurrent());
			progress.setString(BigDecimal.valueOf(progress.getPercentComplete()*100).setScale(2, RoundingMode.HALF_UP) +"%");
		}
	}
	
	@Override
	protected void done() {
		if(progress != null) {
			progress.setValue(progress.getMaximum());
			progress.setVisible(false);
		}
	}
}

@AllArgsConstructor
@Data
class ProgressDTO {
	private int min;
	private int max;
	private int current;
}