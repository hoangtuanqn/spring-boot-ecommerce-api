package mst.local.mstsoftware.modules.products.services.interfaces;

import mst.local.mstsoftware.modules.products.requests.CreateProductRequest;
import mst.local.mstsoftware.modules.products.requests.UpdateProductRequest;
import mst.local.mstsoftware.modules.products.resources.ProductResource;
import org.springframework.data.domain.Page;

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
