package mst.local.mstsoftware.modules.users.services.interfaces;

import mst.local.mstsoftware.modules.users.entities.UserCatalogue;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.CreateUserCatalogueRequest;

public interface UserCatalogueServiceInterface {
    public UserCatalogue create(CreateUserCatalogueRequest userCatalogue);
}
