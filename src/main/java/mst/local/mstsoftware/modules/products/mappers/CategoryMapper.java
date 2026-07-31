package mst.local.mstsoftware.modules.products.mappers;

import mst.local.mstsoftware.mappers.ReadMapper;
import mst.local.mstsoftware.modules.products.entities.Category;
import mst.local.mstsoftware.modules.products.resources.CategoryResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends ReadMapper<Category, CategoryResource> {
}
