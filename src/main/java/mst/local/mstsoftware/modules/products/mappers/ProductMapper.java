package mst.local.mstsoftware.modules.products.mappers;

import mst.local.mstsoftware.mappers.CreateMapper;
import mst.local.mstsoftware.mappers.ReadMapper;
import mst.local.mstsoftware.modules.products.entities.Product;
import mst.local.mstsoftware.modules.products.requests.CreateProductRequest;
import mst.local.mstsoftware.modules.products.resources.ProductResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends ReadMapper<Product, ProductResource>, CreateMapper<Product, CreateProductRequest> {
}
