package mst.local.mstsoftware.modules.products.mappers;

import mst.local.mstsoftware.mappers.BaseMapper;
import mst.local.mstsoftware.modules.products.entities.Category;
import mst.local.mstsoftware.modules.products.requests.CreateCategoryRequest;
import mst.local.mstsoftware.modules.products.requests.UpdateCategoryRequest;
import mst.local.mstsoftware.modules.products.resources.CategoryResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<Category, CategoryResource, CreateCategoryRequest, UpdateCategoryRequest> {
}
