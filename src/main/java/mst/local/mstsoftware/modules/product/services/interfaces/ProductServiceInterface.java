package mst.local.mstsoftware.modules.product.services.interfaces;

import org.springframework.data.domain.Page;

import mst.local.mstsoftware.modules.product.requests.CreateProductRequest;
import mst.local.mstsoftware.modules.product.requests.UpdateProductRequest;
import mst.local.mstsoftware.modules.product.resources.ProductResource;

import java.util.List;
import java.util.Map;

public interface ProductServiceInterface {
    public ProductResource store(CreateProductRequest productRequest);

    public ProductResource findById(Long id);

    public ProductResource update(Long id, UpdateProductRequest productRequest);

    public ProductResource destroy(Long id);

    public void deleteMultiple(List<Long> ids);

    public Page<ProductResource> paginate(Map<String, String[]> parameters);
}
