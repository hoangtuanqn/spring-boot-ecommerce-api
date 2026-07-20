package mst.local.mstsoftware.modules.users.services.interfaces;

import mst.local.mstsoftware.modules.users.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.resources.UserCatalogueResource;

public interface UserCatalogueServiceInterface {
    public UserCatalogueResource create(CreateUserCatalogueRequest userCatalogue);
}
