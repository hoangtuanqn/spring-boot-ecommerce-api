package mst.local.mstsoftware.modules.products.services.interfaces;

import mst.local.mstsoftware.modules.products.requests.CreateCategoryRequest;
import mst.local.mstsoftware.modules.products.requests.UpdateCategoryRequest;
import mst.local.mstsoftware.modules.products.resources.CategoryResource;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface CategoryServiceInterface {
    public List<CategoryResource> list();

    public CategoryResource store(CreateCategoryRequest categoryRequest);

    public CategoryResource findById(Long id);

    public CategoryResource update(Long id, UpdateCategoryRequest categoryRequest);

    public CategoryResource destroy(Long id);

    public void deleteMultiple(List<Long> ids);

    public Page<CategoryResource> paginate(Map<String, String[]> parameters);

}
