package bk.edu.controller;

import bk.edu.data.mapper.CategoryMapper;
import bk.edu.data.req.CategoryRequest;
import bk.edu.data.response.base.MyResponse;
import bk.edu.data.response.dto.AuthorDto;
import bk.edu.data.response.dto.CategoryDto;
import bk.edu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import bk.edu.data.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryMapper categoryMapper;

    @RequestMapping(value = "/api/v1/category", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthor(@RequestBody CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = categoryService.createCategory(categoryRequest);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(categoryMapper.categoryEntityToDto(categoryEntity))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/v1/category/{pageId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthor(@PathVariable int pageId) {
        Pageable pageable = PageRequest.of(pageId, 10, Sort.by("createdAt").descending());
        Page<CategoryEntity> categoryEntityPage = categoryService.getListCategory(pageable);
        List<CategoryDto> categoryDtoList = categoryMapper.listCategoryEntityToDto(categoryEntityPage.toList());
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(categoryDtoList)
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/v1/category/{categoryId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCategory(@PathVariable int categoryId,
                                            @RequestBody CategoryRequest categoryRequest){
        CategoryEntity categoryEntity = categoryService.updateCategory(categoryRequest, categoryId);
        CategoryDto categoryDto = categoryMapper.categoryEntityToDto(categoryEntity);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(categoryDto)
                .get();
        return ResponseEntity.ok(response);
    }
}
