package github.tdonuk.notemanager.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoggingUtils {
	private static String LOG_PARAMS_TEMPLATE = "%s: '%s'";
	
	public static String formatParams(String message, Map<String, Object> params) {
		if(params == null || params.isEmpty()) return message;
		
		StringJoiner stringJoiner = new StringJoiner(",", "{", "}");
		
		for(Entry<String, Object> entry : params.entrySet()) {
			stringJoiner.add(String.format(LOG_PARAMS_TEMPLATE, entry.getKey(), entry.getValue()));
		}
		
		return stringJoiner.toString();
	}
}
