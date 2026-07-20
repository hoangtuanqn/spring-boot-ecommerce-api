package mst.local.mstsoftware.modules.users.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.modules.users.entities.UserCatalogue;
import mst.local.mstsoftware.modules.users.repositories.UserCatalogueRepository;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.services.interfaces.UserCatalogueServiceInterface;
import mst.local.mstsoftware.services.impl.BaseService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserCataloguesService extends BaseService implements UserCatalogueServiceInterface {
    private final UserCatalogueRepository userCatalogueRepository;

    @Override
    @Transactional
    public UserCatalogue create(CreateUserCatalogueRequest userCatalogueRequest) {
        UserCatalogue userCatalogue = UserCatalogue.builder()
                .name(userCatalogueRequest.getName())
                .publish(userCatalogueRequest.getPublish())
                .build();
        return userCatalogueRepository.save(userCatalogue);
    }
}
