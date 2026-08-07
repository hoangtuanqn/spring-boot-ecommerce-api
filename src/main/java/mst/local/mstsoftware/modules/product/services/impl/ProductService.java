package mst.local.mstsoftware.modules.product.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.helpers.CrudValidationHelpers;
import mst.local.mstsoftware.helpers.FileHelper;
import mst.local.mstsoftware.helpers.QuerySpecBuilder;
import mst.local.mstsoftware.modules.product.entities.Product;
import mst.local.mstsoftware.modules.product.mappers.ProductMapper;
import mst.local.mstsoftware.modules.product.repositories.CategoryRepository;
import mst.local.mstsoftware.modules.product.repositories.ProductRepository;
import mst.local.mstsoftware.modules.product.requests.CreateProductRequest;
import mst.local.mstsoftware.modules.product.requests.UpdateProductRequest;
import mst.local.mstsoftware.modules.product.resources.ProductResource;
import mst.local.mstsoftware.modules.product.services.interfaces.ProductServiceInterface;
import mst.local.mstsoftware.services.impl.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService extends BaseService implements ProductServiceInterface {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final QuerySpecBuilder specBuilder;
    private final FileHelper fileHelper;


    @Override
    public ProductResource store(CreateProductRequest productRequest) {
        var category = findOrThrow(categoryRepository.findById(productRequest.categoryId()), "Danh mục không tồn tại!");
        Product product = productMapper.toEntity(productRequest);
        product.setCategory(category);
        List<String> paths = fileHelper.uploadFile(productRequest.photos(), "products");
        product.setImages(paths);
        return productMapper.toResource(productRepository.save(product));
    }

    @Override
    public ProductResource findById(Long id) {
        var product = findOrThrow(productRepository.findById(id), "Sản phẩm này không tồn tại!");
        return productMapper.toResource(product);
    }

    @Override
    public ProductResource update(Long id, UpdateProductRequest productRequest) {
        var product = findOrThrow(productRepository.findById(id), "Sản phẩm này không tồn tại!");
        var category = findOrThrow(categoryRepository.findById(productRequest.categoryId()), "Danh mục không tồn tại!");
        productMapper.updateEntity(productRequest, product);
        product.setCategory(category);
        return productMapper.toResource(productRepository.save(product));
    }

    @Override
    public ProductResource destroy(Long id) {
        var product = findOrThrow(productRepository.findById(id), "Sản phẩm này không tồn tại!");
        productRepository.delete(product);
        return productMapper.toResource(product);
    }

    @Override
    public void deleteMultiple(List<Long> ids) {
        CrudValidationHelpers.deleteMultipleOrThrow(productRepository, ids, Product::getId, "Product");
    }

    @Override
    public Page<ProductResource> paginate(Map<String, String[]> parameters) {
        Specification<Product> specs = specBuilder.buildSpecification(parameters, "title", "description");
        Pageable pageable = specBuilder.buildPageable(parameters);
        return productRepository.findAll(specs, pageable).map(productMapper::toResource);
    }

}
