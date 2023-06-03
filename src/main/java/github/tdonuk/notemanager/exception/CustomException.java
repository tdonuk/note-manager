package github.tdonuk.notemanager.exception;

public class CustomException extends RuntimeException {
	public CustomException(Exception e) {
		super(e);
	}
	
	public CustomException(String msg) {
		super(new Exception(msg));
	}
}
