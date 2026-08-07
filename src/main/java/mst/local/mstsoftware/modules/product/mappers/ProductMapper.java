package mst.local.mstsoftware.modules.product.mappers;

import mst.local.mstsoftware.mappers.BaseMapper;
import mst.local.mstsoftware.modules.product.entities.Product;
import mst.local.mstsoftware.modules.product.requests.CreateProductRequest;
import mst.local.mstsoftware.modules.product.requests.UpdateProductRequest;
import mst.local.mstsoftware.modules.product.resources.ProductResource;

import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<Product, ProductResource, CreateProductRequest, UpdateProductRequest> {

    @Override
    @Mapping(target = "images", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateProductRequest updateRequest, @MappingTarget Product entity);


    default List<String> map(List<MultipartFile> files) {
        return new ArrayList<>();
    }
}
