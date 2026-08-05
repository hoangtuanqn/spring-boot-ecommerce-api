package mst.local.mstsoftware.helpers;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterParameterHelper {

    // phân loại theo keyword
    public static String filterKeyword(Map<String, String[]> parameters) {
        return parameters.containsKey("keyword") ? parameters.get("keyword")[0] : null;
    }

    // phân loại theo các filter mà đơn giản
    public static Map<String, String> filterSimple(Map<String, String[]> parameters) {
        String[] arr = {"keyword", "page", "perPage", "sort", "start_date", "end_date"};
        return parameters.entrySet().stream()
                .filter(entry -> !Arrays.asList(arr).contains(entry.getKey()))
                .filter(entry -> !entry.getKey().contains("["))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()[0]));

    }

    // phân loại theo các filter phức tạp
    public static Map<String, Map<String, String>> filterComplex(Map<String, String[]> parameters) {
        return parameters.entrySet().stream().filter(entry -> entry.getKey().contains("["))
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().split("\\[")[0],
                        Collectors.toMap(
                                entry -> entry.getKey().split("\\[")[1].replace("]", ""),
                                entry -> entry.getValue()[0]
                        )
                ));
    }

    // phân loại theo filter kiểu ngày tháng
    public static Map<String, String> filterDateRange(Map<String, String[]> parameters) {
        Map<String, String> dateRangeFilters = new HashMap<>();
        if (StringUtils.hasText(parameters.get("start_date")[0])) {
            dateRangeFilters.put("start_date", parameters.get("start_date")[0]);
        }
        if (StringUtils.hasText(parameters.get("end_date")[0])) {
            dateRangeFilters.put("end_date", parameters.get("end_date")[0]);
        }
        return dateRangeFilters;
    }
}
