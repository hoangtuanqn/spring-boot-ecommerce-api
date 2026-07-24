package mst.local.mstsoftware.specifications;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class BaseSpecification<T> {

    public static <T> Specification<T> keyword(String keyword, String... fields) {
        // root: đại diện cho table, trỏ vô column để lấy dữ liệu
        // query: đại diện cho câu lệnh query tổng thể
        // cb (CriteriaBuilder): Factory tạo ra các Predicate (Điều kiện SQL)
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            Predicate[] predicates = Arrays.stream(fields)
                    .map(f -> cb.like(cb.lower(root.get(f)), pattern))
                    .toArray(Predicate[]::new);
            return cb.or(predicates);
        };
    }

    public static <T> Specification<T> whereSpec(Map<String, String> filters) {
        return (root, query, cb) -> {
            if (filters == null || filters.isEmpty()) return cb.conjunction();
            Predicate[] predicates = filters.entrySet().stream()
                    .filter(f -> f.getValue() != null && !f.getValue().isEmpty())
                    .map(f -> cb.equal(root.get(f.getKey()), f.getValue()))
                    .toArray(Predicate[]::new);

            return cb.and(predicates);
        };
    }

    public static <T> Specification<T> createdBetween(Instant from, Instant to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return cb.conjunction();
            if (from == null) return cb.lessThanOrEqualTo(root.get("createdAt"), to);
            if (to == null) return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            return cb.between(root.get("createdAt"), from, to);
        };
    }

    public static <T> Specification<T> whereComplex(Map<String, Map<String, String>> filters) {
        return (root, query, cb) -> {
            if (filters == null || filters.isEmpty()) return cb.conjunction();

            Predicate[] fieldPredicates = filters.entrySet().stream()
                    .map(fieldEntry -> {
                        String field = fieldEntry.getKey();
                        Map<String, String> ops = fieldEntry.getValue();

                        Predicate[] opPredicates = ops.entrySet().stream()
                                .filter(op -> op.getValue() != null && !op.getValue().isEmpty())
                                .map(op -> buildPredicate(root, cb, field, op.getKey(), op.getValue()))
                                .filter(Objects::nonNull)
                                .toArray(Predicate[]::new);
                        return cb.and(opPredicates);
                    }).toArray(Predicate[]::new);
            return cb.and(fieldPredicates);

        };
    }

    private static <T> Predicate buildPredicate(
            jakarta.persistence.criteria.Root<T> root,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            String field,
            String operator,
            String value
    ) {
        return switch (operator) {
            case "eq" -> cb.equal(root.get(field), value);
            case "neq" -> cb.notEqual(root.get(field), value);
            case "like" -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
            case "gt" -> cb.greaterThan(root.get(field), value);
            case "lt" -> cb.lessThan(root.get(field), value);
            case "gte" -> cb.greaterThanOrEqualTo(root.get(field), value);
            case "lte" -> cb.lessThanOrEqualTo(root.get(field), value);
            case "in" -> root.get(field).in(Arrays.asList(value.split(",")));
            default -> null;
        };
    }

}
