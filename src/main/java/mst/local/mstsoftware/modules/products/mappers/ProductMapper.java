package mst.local.mstsoftware.modules.products.mappers;

import mst.local.mstsoftware.mappers.BaseMapper;
import mst.local.mstsoftware.modules.products.entities.Product;
import mst.local.mstsoftware.modules.products.requests.CreateProductRequest;
import mst.local.mstsoftware.modules.products.requests.UpdateProductRequest;
import mst.local.mstsoftware.modules.products.resources.ProductResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<Product, ProductResource, CreateProductRequest, UpdateProductRequest> {
}
