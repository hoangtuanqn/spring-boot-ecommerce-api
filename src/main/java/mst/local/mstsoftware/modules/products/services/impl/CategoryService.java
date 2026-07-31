package mst.local.mstsoftware.modules.products.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.helpers.CrudValidationHelpers;
import mst.local.mstsoftware.helpers.QuerySpecBuilder;
import mst.local.mstsoftware.modules.products.entities.Category;
import mst.local.mstsoftware.modules.products.mappers.CategoryMapper;
import mst.local.mstsoftware.modules.products.repositories.CategoryRepository;
import mst.local.mstsoftware.modules.products.requests.CreateCategoryRequest;
import mst.local.mstsoftware.modules.products.requests.UpdateCategoryRequest;
import mst.local.mstsoftware.modules.products.resources.CategoryResource;
import mst.local.mstsoftware.modules.products.services.interfaces.CategoryServiceInterface;
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
public class CategoryService extends BaseService implements CategoryServiceInterface {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final QuerySpecBuilder specBuilder;

    @Override
    public List<CategoryResource> list() {
        return categoryMapper.toList(categoryRepository.findAll());
    }

    @Override
    public CategoryResource store(CreateCategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        return categoryMapper.toResource(categoryRepository.save(category));
    }

    @Override
    public CategoryResource update(Long id, UpdateCategoryRequest categoryRequest) {
        var category = findOrThrow(categoryRepository.findById(id), "Không tìm thấy danh mục!");
        categoryMapper.updateEntity(categoryRequest, category);
        return categoryMapper.toResource(categoryRepository.save(category));
    }

    @Override
    public CategoryResource destroy(Long id) {
        var category = findOrThrow(categoryRepository.findById(id), "Không tìm thấy danh mục!");
        categoryRepository.delete(category);
        return categoryMapper.toResource(category);
    }

    @Override
    public void deleteMultiple(List<Long> ids) {
        CrudValidationHelpers.deleteMultipleOrThrow(categoryRepository, ids, Category::getId, "Category");
    }

    @Override
    public Page<CategoryResource> paginate(Map<String, String[]> parameters) {
        Specification<Category> specs = specBuilder.buildSpecification(parameters, "name");
        Pageable pageable = specBuilder.buildPageable(parameters);
        return categoryRepository.findAll(specs, pageable).map(categoryMapper::toResource);
    }

}
