package mst.local.mstsoftware.modules.user.mappers;

import mst.local.mstsoftware.mappers.BaseMapper;
import mst.local.mstsoftware.modules.user.entities.UserCatalogue;
import mst.local.mstsoftware.modules.user.requests.UserCatagoue.CreateUserCatalogueRequest;
import mst.local.mstsoftware.modules.user.requests.UserCatagoue.UpdateUserCatalogueRequest;
import mst.local.mstsoftware.modules.user.resources.UserCatalogueResource;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCatalogueMapper extends BaseMapper<UserCatalogue, UserCatalogueResource, CreateUserCatalogueRequest, UpdateUserCatalogueRequest> {

}
