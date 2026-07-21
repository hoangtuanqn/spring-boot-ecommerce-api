package mst.local.mstsoftware.modules.users.services.interfaces;

import mst.local.mstsoftware.modules.users.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.UpdateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.resources.UserCatalogueResource;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface UserCatalogueServiceInterface {
    public UserCatalogueResource create(CreateUserCatalogueRequest userCatalogue);

    public UserCatalogueResource update(Long id, UpdateUserCatalogueRequest userCatalogue);

    public Page<UserCatalogueResource> paginate(Map<String, String[]> parameters);
}
