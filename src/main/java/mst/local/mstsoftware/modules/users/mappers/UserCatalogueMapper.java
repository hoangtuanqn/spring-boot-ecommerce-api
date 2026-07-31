package mst.local.mstsoftware.modules.users.mappers;

import mst.local.mstsoftware.mappers.BaseMapper;
import mst.local.mstsoftware.modules.users.entities.UserCatalogue;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.requests.UserCatagoue.UpdateUserCatalogueRequest;
import mst.local.mstsoftware.modules.users.resources.UserCatalogueResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCatalogueMapper extends BaseMapper<UserCatalogue, UserCatalogueResource, CreateUserCatalogueRequest, UpdateUserCatalogueRequest> {

}
