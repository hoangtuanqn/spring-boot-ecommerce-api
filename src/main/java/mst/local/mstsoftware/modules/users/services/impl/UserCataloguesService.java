package mst.local.mstsoftware.modules.users.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.modules.users.entities.UserCatalogue;
import mst.local.mstsoftware.modules.users.repositories.UserCatalogueRepository;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.UpdateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.resources.UserCatalogueResource;
import mst.local.mstsoftware.modules.users.services.interfaces.UserCatalogueServiceInterface;
import mst.local.mstsoftware.services.impl.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class UserCataloguesService extends BaseService implements UserCatalogueServiceInterface {
    private final UserCatalogueRepository userCatalogueRepository;

    @Override
    @Transactional
    public UserCatalogueResource create(CreateUserCatalogueRequest userCatalogueRequest) {
        UserCatalogue userCatalogue = UserCatalogue.builder()
                .name(userCatalogueRequest.getName())
                .publish(userCatalogueRequest.getPublish())
                .build();
        UserCatalogue created = userCatalogueRepository.save(userCatalogue);
        return UserCatalogueResource.fromEntity(created);
    }

    @Override
    @Transactional
    public UserCatalogueResource update(Long id, UpdateUserCatalogueRequest userCatalogue) {
        UserCatalogue userCata = userCatalogueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nguười dùng!"));

        userCata.setName(userCatalogue.getName());
        userCata.setPublish(userCatalogue.getPublish());

        UserCatalogue updated = userCatalogueRepository.save(userCata);

        return UserCatalogueResource.fromEntity(updated);
    }

    @Override
    public Page<UserCatalogueResource> paginate(Map<String, String[]> parameters) {
        int page = parameters.containsKey("page") ? Integer.parseInt(parameters.get("page")[0]) : 1;
        int perPage = parameters.containsKey("perPage") ? Integer.parseInt(parameters.get("perPage")[0]) : 20;
        Sort sort = createSort(parameters.get("sort") != null ? parameters.get("sort")[0] : "id");

        Pageable pageable = PageRequest.of(page - 1, perPage, sort);
        return userCatalogueRepository.findAll(pageable).map(UserCatalogueResource::fromEntity);
    }

}
