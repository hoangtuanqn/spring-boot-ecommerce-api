package mst.local.mstsoftware.modules.product.mappers;

import mst.local.mstsoftware.mappers.BaseMapper;
import mst.local.mstsoftware.modules.product.entities.Category;
import mst.local.mstsoftware.modules.product.requests.CreateCategoryRequest;
import mst.local.mstsoftware.modules.product.requests.UpdateCategoryRequest;
import mst.local.mstsoftware.modules.product.resources.CategoryResource;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<Category, CategoryResource, CreateCategoryRequest, UpdateCategoryRequest> {
}
