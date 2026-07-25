package mst.local.mstsoftware.modules.users.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.filters.FilterParameter;
import mst.local.mstsoftware.helpers.Helpers;
import mst.local.mstsoftware.modules.users.entities.UserCatalogue;
import mst.local.mstsoftware.modules.users.mappers.UserCatalogueMapper;
import mst.local.mstsoftware.modules.users.repositories.UserCatalogueRepository;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.UpdateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.resources.UserCatalogueResource;
import mst.local.mstsoftware.modules.users.services.interfaces.UserCatalogueServiceInterface;
import mst.local.mstsoftware.services.impl.BaseService;
import mst.local.mstsoftware.specifications.BaseSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class UserCataloguesService extends BaseService implements UserCatalogueServiceInterface {
    private final UserCatalogueRepository userCatalogueRepository;
    private final UserCatalogueMapper userCatalogueMapper;


    @Override
    @Transactional
    public UserCatalogueResource store(CreateUserCatalogueRequest userCatalogueRequest) {
        UserCatalogue userCatalogue = userCatalogueMapper.toEntity(userCatalogueRequest);
        UserCatalogue created = userCatalogueRepository.save(userCatalogue);
        return userCatalogueMapper.toResource(created);
    }

    @Override
    @Transactional
    public UserCatalogueResource update(Long id, UpdateUserCatalogueRequest userCatalogue) {
        UserCatalogue userCata = userCatalogueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nguười dùng!"));
        userCatalogueMapper.updateEntity(userCatalogue, userCata);
        UserCatalogue updated = userCatalogueRepository.save(userCata);

        return userCatalogueMapper.toResource(updated);
    }

    public UserCatalogueResource findById(Long id) {
        UserCatalogue userCatalogue = userCatalogueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user catalogue này!"));
        return userCatalogueMapper.toResource(userCatalogue);
    }

    public List<UserCatalogueResource> list() {
        return userCatalogueMapper.toList(userCatalogueRepository.findAll());
    }

    public UserCatalogueResource destroy(Long id) {
        UserCatalogue userCatalogue = userCatalogueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user catalogue này!"));
        userCatalogueRepository.delete(userCatalogue);
        return userCatalogueMapper.toResource(userCatalogue);
    }

    @Override
    public Page<UserCatalogueResource> paginate(Map<String, String[]> parameters) {
        int page = Math.max(1, Helpers.parseIntSafe(parameters.get("page"), 1));
        int perPage = Math.clamp(Helpers.parseIntSafe(parameters.get("perPage"), 20), 1, 100);
        Sort sort = createSort(parameters.get("sort") != null ? parameters.get("sort")[0] : "id");
        String keyword = FilterParameter.filterKeyword(parameters);
        Map<String, String> filterSimple = FilterParameter.filterSimple(parameters);
        Map<String, Map<String, String>> filterComplex = FilterParameter.filterComplex(parameters);

        Specification<UserCatalogue> specs = Specification.where(
                        BaseSpecification.<UserCatalogue>keyword(keyword, "name")
                ).and(BaseSpecification.<UserCatalogue>whereSpec(filterSimple))
                .and(BaseSpecification.<UserCatalogue>whereComplex(filterComplex));
        Pageable pageable = PageRequest.of(page - 1, perPage, sort);
        return userCatalogueRepository.findAll(specs, pageable).map(userCatalogueMapper::toResource);
    }

}
