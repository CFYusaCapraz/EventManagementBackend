package com.yusacapraz.event.controller;

import com.yusacapraz.event.response.APIResponse;
import com.yusacapraz.event.model.DTOs.CategoryCreateDTO;
import com.yusacapraz.event.model.DTOs.CategoryUpdateDTO;
import com.yusacapraz.event.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<APIResponse<?>> getCategoryByCategoryId(@PathVariable("categoryId") String categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping
    public ResponseEntity<APIResponse<?>> createCategory(@RequestBody CategoryCreateDTO categoryCreateDTO) {
        return categoryService.createCategory(categoryCreateDTO);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<APIResponse<?>> updateCategory(@PathVariable("categoryId") String categoryId,
                                                         @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        return categoryService.updateCategory(categoryId, categoryUpdateDTO);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<APIResponse<?>> deleteCategory(@PathVariable("categoryId") String categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

}
