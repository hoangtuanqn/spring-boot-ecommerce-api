package mst.local.mstsoftware.helpers;

import mst.local.mstsoftware.specifications.BaseSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public final class QuerySpecBuilder {

    public <T> Specification<T> buildSpecification(Map<String, String[]> parameters, String... searchFields) {
        String keyword = FilterParameterHelper.filterKeyword(parameters);
        Map<String, String> filterSimple = FilterParameterHelper.filterSimple(parameters);
        Map<String, Map<String, String>> filterComplex = FilterParameterHelper.filterComplex(parameters);
        return Specification.where(
                        BaseSpecification.<T>keyword(keyword, searchFields)
                ).and(BaseSpecification.<T>whereSpec(filterSimple))
                .and(BaseSpecification.<T>whereComplex(filterComplex));

    }

    public Pageable buildPageable(Map<String, String[]> parameters) {
        int page = Math.max(1, Helpers.parseIntSafe(parameters.get("page"), 1));
        int perPage = Math.clamp(Helpers.parseIntSafe(parameters.get("perPage"), 20), 1, 100);
        return PageRequest.of(page - 1, perPage, buildSort(parameters));
    }

    private Sort buildSort(Map<String, String[]> parameters) {
        String sortParam = parameters.get("sort") != null ? parameters.get("sort")[0] : null;
        if (!StringUtils.hasText(sortParam)) {
            return Sort.by(Sort.Order.desc("id"));
        }
        String[] parts = sortParam.split(",");
        String field = parts[0];
        String sortDirection = (parts.length > 1) ? parts[parts.length - 1] : "DESC";
        return "desc".equalsIgnoreCase(sortDirection)
                ? Sort.by(Sort.Order.desc(field))
                : Sort.by(Sort.Order.asc(field));

    }
}
