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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryMapper categoryMapper;

    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = categoryService.createCategory(categoryRequest);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(categoryMapper.categoryEntityToDto(categoryEntity))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/detail/category/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getDetailCategory(@PathVariable int id) {
        CategoryEntity categoryEntity = categoryService.getDetailCategory(id);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(categoryMapper.categoryEntityToDto(categoryEntity))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/category", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = categoryService.updateCategory(categoryRequest);

        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(categoryMapper.categoryEntityToDto(categoryEntity))
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/category/{pageId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthor(@PathVariable int pageId) {
        Pageable pageable = PageRequest.of(pageId - 1, 10, Sort.by("createdAt").descending());
        Page<CategoryEntity> categoryEntityPage = categoryService.getListCategory(pageable);
        List<CategoryDto> categoryDtoList = categoryMapper.listCategoryEntityToDto(categoryEntityPage.toList());

        Map<String, Object> mapReturn = new HashMap<>();
        mapReturn.put("categories", categoryDtoList);
        mapReturn.put("totalPage", categoryEntityPage.getTotalPages());
        mapReturn.put("pageSize", 10);
        mapReturn.put("pageOffset", pageId);
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(mapReturn)
                .get();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/all/category", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCategory() {
        List<CategoryEntity> categoryEntities = categoryService.getAllCategory();
        MyResponse response = MyResponse
                .builder()
                .buildCode(200)
                .buildMessage("Successfully")
                .buildData(categoryMapper.listCategoryEntityToDto(categoryEntities))
                .get();
        return ResponseEntity.ok(response);
    }
}
