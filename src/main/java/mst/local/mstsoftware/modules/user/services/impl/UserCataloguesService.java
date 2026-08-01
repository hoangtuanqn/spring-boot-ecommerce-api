package mst.local.mstsoftware.modules.user.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.helpers.CrudValidationHelpers;
import mst.local.mstsoftware.helpers.QuerySpecBuilder;
import mst.local.mstsoftware.modules.user.entities.UserCatalogue;
import mst.local.mstsoftware.modules.user.mappers.UserCatalogueMapper;
import mst.local.mstsoftware.modules.user.repositories.UserCatalogueRepository;
import mst.local.mstsoftware.modules.user.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.user.requests.UserCatagoue.UpdateUserCatalogueRequest;
import mst.local.mstsoftware.modules.user.resources.UserCatalogueResource;
import mst.local.mstsoftware.modules.user.services.interfaces.UserCatalogueServiceInterface;
import mst.local.mstsoftware.services.impl.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final QuerySpecBuilder specBuilder;


    @Override
    @Transactional
    public UserCatalogueResource store(CreateUserCatalogueRequest userCatalogueRequest) {
        UserCatalogue userCatalogue = userCatalogueMapper.toEntity(userCatalogueRequest);
        return userCatalogueMapper.toResource(userCatalogueRepository.save(userCatalogue));
    }

    @Override
    @Transactional
    public UserCatalogueResource update(Long id, UpdateUserCatalogueRequest userCatalogueRequest) {
        var userCatalogue = findOrThrow(userCatalogueRepository.findById(id), "Không tìm thấy nguười dùng!");
        userCatalogueMapper.updateEntity(userCatalogueRequest, userCatalogue);

        return userCatalogueMapper.toResource(userCatalogueRepository.save(userCatalogue));
    }

    public UserCatalogueResource findById(Long id) {
        var userCatalogue = findOrThrow(userCatalogueRepository.findById(id), "Không tìm thấy use   r catalogue này!");
        return userCatalogueMapper.toResource(userCatalogue);
    }

    public List<UserCatalogueResource> list() {
        return userCatalogueMapper.toList(userCatalogueRepository.findAll());
    }

    public UserCatalogueResource destroy(Long id) {
        var userCatalogue = findOrThrow(userCatalogueRepository.findById(id), "Không tìm thấy user catalogue này!");
        userCatalogueRepository.delete(userCatalogue);
        return userCatalogueMapper.toResource(userCatalogue);
    }

    @Override
    @Transactional
    public void deleteMultiple(List<Long> ids) {
        CrudValidationHelpers.deleteMultipleOrThrow(userCatalogueRepository, ids, UserCatalogue::getId, "User Catalogue");
    }

    @Override
    public Page<UserCatalogueResource> paginate(Map<String, String[]> parameters) {
        Specification<UserCatalogue> specs = specBuilder.buildSpecification(parameters, "name");
        Pageable pageable = specBuilder.buildPageable(parameters);
        return userCatalogueRepository.findAll(specs, pageable).map(userCatalogueMapper::toResource);
    }

}
