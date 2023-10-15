package github.tdonuk.notemanager.gui.dialog;

import github.tdonuk.notemanager.domain.FileHist;
import github.tdonuk.notemanager.util.HistoryCache;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.Comparator;
import java.util.Vector;
import java.util.function.Consumer;

@Slf4j
public class HistoryDialog extends SelectionDialog<FileHist> {
	public HistoryDialog(Point location, Consumer<FileHist> selectionHandler) {
		super(location, selectionHandler);
		
		pack();
	}
	
	@Override
	protected JList<FileHist> listComponent() {
		JList<FileHist> historyRowList = new JList<>(new Vector<>(HistoryCache.get().stream().sorted(Comparator.comparing(FileHist::getTitle)).toList()));
		historyRowList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel rendererLabel && value instanceof FileHist fileHist) {
					rendererLabel.setText(fileHist.getTitle());
					rendererLabel.setIcon(FileSystemView.getFileSystemView().getSystemIcon(new File(fileHist.getOpenedFile())));
					rendererLabel.setHorizontalTextPosition(SwingConstants.TRAILING);
				}
				
				return renderer;
			}
		});
		
		historyRowList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		return historyRowList;
	}
}
