package mst.local.mstsoftware.modules.products.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.helpers.QuerySpecBuilder;
import mst.local.mstsoftware.modules.products.entities.Product;
import mst.local.mstsoftware.modules.products.mappers.ProductMapper;
import mst.local.mstsoftware.modules.products.repositories.CategoryRepository;
import mst.local.mstsoftware.modules.products.repositories.ProductRepository;
import mst.local.mstsoftware.modules.products.requests.CreateProductRequest;
import mst.local.mstsoftware.modules.products.resources.ProductResource;
import mst.local.mstsoftware.modules.products.services.interfaces.ProductServiceInterface;
import mst.local.mstsoftware.services.impl.BaseService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService extends BaseService implements ProductServiceInterface {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final QuerySpecBuilder specBuilder;


    @Override
    public ProductResource store(CreateProductRequest productRequest) {
        var category = findOrThrow(categoryRepository.findById(productRequest.categoryId()), "Danh mục không tồn tại!");
        Product product = productMapper.toEntity(productRequest);
        product.setCategory(category);
        return productMapper.toResource(productRepository.save(product));
    }

    @Override
    public ProductResource findById(Long id) {
        var product = findOrThrow(productRepository.findById(id), "Sản phẩm này không tồn tại!");
        return productMapper.toResource(product);
    }

}
