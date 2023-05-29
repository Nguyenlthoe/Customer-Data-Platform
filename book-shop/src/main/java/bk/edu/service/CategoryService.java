package bk.edu.service;

import bk.edu.data.entity.CategoryEntity;
import bk.edu.data.mapper.CategoryMapper;
import bk.edu.data.req.CategoryRequest;
import bk.edu.exception.RequestInvalid;
import bk.edu.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public List<CategoryEntity> getAllCategory(){
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        return categoryEntities;
    }

    public CategoryEntity updateCategory(CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = categoryRepository.findByCategoryId(categoryRequest.getId());
        if(categoryEntity == null){
            throw new RequestInvalid("Category not found");
        }
        categoryEntity.setName(categoryRequest.getName());
        categoryEntity.setUpdatedAt(new Date());
        categoryRepository.saveAndFlush(categoryEntity);
        return categoryEntity;
    }
}
