package github.tdonuk.notemanager.util;

import github.tdonuk.notemanager.domain.FileHist;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HistoryCache {
	private static final List<FileHist> cache = new ArrayList<>();
	
	public static void addIfAbsent(FileHist fileHist) {
		if(!cache.contains(fileHist)) cache.add(fileHist);
	}
	
	public static List<FileHist> get() {
		return cache;
	}
	
	public static FileHist getByFile(File file) {
		return cache.stream().filter(fh -> fh.getOpenedFile().equals(file.getAbsolutePath())).findAny().orElse(null);
	}
}
