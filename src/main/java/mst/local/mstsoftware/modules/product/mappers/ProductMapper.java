package mst.local.mstsoftware.modules.product.mappers;

import mst.local.mstsoftware.mappers.BaseMapper;
import mst.local.mstsoftware.modules.product.entities.Product;
import mst.local.mstsoftware.modules.product.requests.CreateProductRequest;
import mst.local.mstsoftware.modules.product.requests.UpdateProductRequest;
import mst.local.mstsoftware.modules.product.resources.ProductResource;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<Product, ProductResource, CreateProductRequest, UpdateProductRequest> {
}
