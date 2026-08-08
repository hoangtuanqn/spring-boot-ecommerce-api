package mst.local.mstsoftware.helpers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Helpers {
    public static Integer parseIntSafe(String value, Integer defaultValue) {
        try {
            return (value != null && !value.isEmpty()) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Integer parseIntSafe(String[] values, Integer defaultValue) {
        return parseIntSafe((values != null && values.length > 0) ? values[0] : null, defaultValue);
    }

    public static String formatDate(Instant date) {
        DateTimeFormatter FMT =
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        return FMT.format(date);
    }
}
