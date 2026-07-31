package mst.local.mstsoftware.modules.products.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.products.entities.Category;
import mst.local.mstsoftware.modules.products.mappers.CategoryMapper;
import mst.local.mstsoftware.modules.products.repositories.CategoryRepository;
import mst.local.mstsoftware.modules.products.requests.CreateCategoryRequest;
import mst.local.mstsoftware.modules.products.requests.UpdateCategoryRequest;
import mst.local.mstsoftware.modules.products.resources.CategoryResource;
import mst.local.mstsoftware.modules.products.services.interfaces.CategoryServiceInterface;
import mst.local.mstsoftware.services.impl.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryService extends BaseService implements CategoryServiceInterface {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

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


}
