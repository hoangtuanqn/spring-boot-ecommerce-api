package mst.local.mstsoftware.helpers;

public class Helpers {
    public static Integer parseIntSafe(String value, Integer defaultValue) {
        try {
            return (value != null && value.isEmpty()) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
