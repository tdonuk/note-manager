package constant;

public enum FileType {
	XML, HTML, TXT;
	
	public static FileType findByExtension(String extension) {
		try {
			return valueOf(extension.toUpperCase());
		} catch(Exception e) {
			return TXT;
		}
	}
}
