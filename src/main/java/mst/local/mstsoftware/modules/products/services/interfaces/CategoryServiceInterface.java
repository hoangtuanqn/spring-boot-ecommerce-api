package mst.local.mstsoftware.modules.products.services.interfaces;

import mst.local.mstsoftware.modules.products.requests.CreateCategoryRequest;
import mst.local.mstsoftware.modules.products.requests.UpdateCategoryRequest;
import mst.local.mstsoftware.modules.products.resources.CategoryResource;

import java.util.List;

public interface CategoryServiceInterface {
    public List<CategoryResource> list();

    public CategoryResource store(CreateCategoryRequest categoryRequest);

    public CategoryResource update(Long id, UpdateCategoryRequest categoryRequest);

    public CategoryResource destroy(Long id);
}
