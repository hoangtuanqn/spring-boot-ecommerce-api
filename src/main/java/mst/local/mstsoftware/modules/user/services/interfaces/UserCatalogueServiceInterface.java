package mst.local.mstsoftware.modules.user.services.interfaces;

import org.springframework.data.domain.Page;

import mst.local.mstsoftware.modules.user.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.user.requests.UserCatagoue.UpdateUserCatalogueRequest;
import mst.local.mstsoftware.modules.user.resources.UserCatalogueResource;

import java.util.List;
import java.util.Map;

public interface UserCatalogueServiceInterface {
    public UserCatalogueResource store(CreateUserCatalogueRequest userCatalogue);

    public UserCatalogueResource update(Long id, UpdateUserCatalogueRequest userCatalogue);

    public Page<UserCatalogueResource> paginate(Map<String, String[]> parameters);

    public List<UserCatalogueResource> list();

    public UserCatalogueResource findById(Long id);

    public UserCatalogueResource destroy(Long id);

    public void deleteMultiple(List<Long> ids);

}
