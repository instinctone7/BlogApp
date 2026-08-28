package com.InstinctOne.BlogApp.controllers;

import com.InstinctOne.BlogApp.dtos.CategoryRequest;
import com.InstinctOne.BlogApp.dtos.TagDto;
import com.InstinctOne.BlogApp.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<TagDto.CategoryDto> createCategory(@RequestBody CategoryRequest request) {
        TagDto.CategoryDto response = categoryService.createCategory(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> delete(CategoryRequest request) {
        categoryService.deleteCategory(request);
        return ResponseEntity.noContent().build();
    }
}
