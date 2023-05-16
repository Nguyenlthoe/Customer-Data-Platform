package bk.edu.service;

import bk.edu.data.entity.CategoryEntity;
import bk.edu.data.mapper.CategoryMapper;
import bk.edu.data.req.CategoryRequest;
import bk.edu.exception.CategoryRequestInvalid;
import bk.edu.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    CategoryRepository categoryRepository;

    public CategoryEntity createCategory(CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = categoryMapper.categoryRequestToDto(categoryRequest);
        categoryRepository.saveAndFlush(categoryEntity);
        return categoryEntity;
    }

    public Page<CategoryEntity> getListCategory(Pageable pageable) {
        Page<CategoryEntity> categoryEntities = categoryRepository.findAll(pageable);
        return categoryEntities;
    }

    public CategoryEntity updateCategory(CategoryRequest categoryRequest, int categoryId){
        CategoryEntity categoryEntity = categoryRepository.findByCategoryId(categoryId);
        if(categoryEntity == null){
            throw new CategoryRequestInvalid("Category not found");
        }

        if(categoryRequest.getName() != null){
            categoryEntity.setName(categoryRequest.getName());
        }
        categoryRepository.saveAndFlush(categoryEntity);
        return categoryEntity;
    }
}
