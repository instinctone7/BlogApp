package com.InstinctOne.BlogApp.services;

import com.InstinctOne.BlogApp.dtos.CategoryRequest;
import com.InstinctOne.BlogApp.dtos.TagDto;
import com.InstinctOne.BlogApp.entities.Category;
import com.InstinctOne.BlogApp.exceptions.CategoryNotFound;
import com.InstinctOne.BlogApp.mappers.MapDtos;
import com.InstinctOne.BlogApp.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MapDtos mapDtos;

    public CategoryService(CategoryRepository categoryRepository, MapDtos mapDtos) {
        this.categoryRepository = categoryRepository;
        this.mapDtos = mapDtos;
    }

    public TagDto.CategoryDto createCategory(CategoryRequest request){
        Category category = new Category();
        category.setName(request.name());
        categoryRepository.save(category);
        return mapDtos.mapCategoryToDto(category);
    }

    public void deleteCategory(CategoryRequest request){
        Category category = categoryRepository.findByName(request.name());
        if (category == null){
            throw new CategoryNotFound("Category "+request.name()+" doesnt exist");
        }
        categoryRepository.delete(category);
    }
}
