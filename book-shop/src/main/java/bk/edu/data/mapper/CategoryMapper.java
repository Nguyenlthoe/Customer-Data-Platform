package bk.edu.data.mapper;

import bk.edu.data.entity.CategoryEntity;
import bk.edu.data.req.CategoryRequest;
import bk.edu.data.response.dto.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryMapper {
    public CategoryDto categoryEntityToDto(CategoryEntity CategoryEntity){
        return new CategoryDto(CategoryEntity.getCategoryId(), CategoryEntity.getName());
    }

    public CategoryEntity categoryRequestToDto(CategoryRequest CategoryRequest){
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(CategoryRequest.getName());
        return categoryEntity;
    }

    public List<CategoryDto> listCategoryEntityToDto(List<CategoryEntity> CategoryEntities){
        List<CategoryDto> categories = new ArrayList<>();
        CategoryEntities.forEach(CategoryEntity -> {
            categories.add(categoryEntityToDto(CategoryEntity));
        });
        return categories;
    }
}
