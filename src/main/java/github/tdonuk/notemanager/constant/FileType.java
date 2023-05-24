package github.tdonuk.notemanager.constant;

import java.util.Arrays;

public enum FileType {
	XML(".xml"), HTML(".html"), TXT(".txt"), JSON(".json"), XSL(".xsl"), XSLT(".xslt");
	
	private final String extension;
	
	public String getExtension() {
		return extension;
	}
	
	FileType(String extension) {
		this.extension = extension;
	}
	
	public static FileType findByExtension(String extension) {
		try {
			return Arrays.stream(values()).filter(v -> v.getExtension().equals(extension)).findAny().orElse(TXT);
		} catch(Exception e) {
			return TXT;
		}
	}
}
