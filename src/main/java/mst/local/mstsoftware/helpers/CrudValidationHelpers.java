package mst.local.mstsoftware.helpers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CrudValidationHelpers {
    public static <T, ID> void deleteMultipleOrThrow(
            JpaRepository<T, ID> repository,
            List<ID> ids,
            Function<T, ID> idExtractor,
            String entityName
    ) {
        var entities = repository.findAllById(ids);
        if (entities.size() != ids.size()) {
            Set<ID> foundIds = entities.stream().map(idExtractor).collect(Collectors.toSet());
            List<ID> notFoundIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();
            throw new EntityNotFoundException("Không tìm thấy " + entityName + "các id sau: " + notFoundIds);
        }
        repository.deleteAll(entities);
    }
}
