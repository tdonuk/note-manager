package github.tdonuk.notemanager.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
	
	public static String formatDate(TemporalAccessor dateTime) {
		return dateFormatter.format(dateTime);
	}
}
