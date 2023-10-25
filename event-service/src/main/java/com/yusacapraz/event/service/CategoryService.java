package com.yusacapraz.event.service;

import com.yusacapraz.event.mapper.CategoryMapper;
import com.yusacapraz.event.model.APIResponse;
import com.yusacapraz.event.model.Category;
import com.yusacapraz.event.model.DTOs.CategoryCreateDTO;
import com.yusacapraz.event.model.DTOs.CategoryUpdateDTO;
import com.yusacapraz.event.model.DTOs.CategoryViewDTO;
import com.yusacapraz.event.model.exception.CategoryNotFoundException;
import com.yusacapraz.event.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<APIResponse<?>> getAllCategories() {
        try {
            List<Category> categoryList = categoryRepository.findAll();
            List<CategoryViewDTO> viewDTOS = new ArrayList<>();
            if (categoryList.isEmpty()) {
                APIResponse<Object> response = APIResponse.error("No category information is found!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            for (Category category : categoryList) {
                viewDTOS.add(CategoryMapper.viewMapper(category));
            }
            APIResponse<List<CategoryViewDTO>> response = APIResponse.successWithData(viewDTOS, "Here are all categories.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> getCategoryById(String categoryId) {
        try {
            UUID uuid = UUID.fromString(categoryId);
            Category category = categoryRepository.findById(uuid)
                    .orElseThrow(() -> new CategoryNotFoundException("Category of the given id not found!"));
            CategoryViewDTO viewDTO = CategoryMapper.viewMapper(category);
            APIResponse<CategoryViewDTO> response = APIResponse.successWithData(viewDTO, "Category of given id is found.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CategoryNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            APIResponse<Object> response = APIResponse.error("Please enter a valid category id!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> createCategory(CategoryCreateDTO categoryCreateDTO) {
        try {
            if (categoryCreateDTO.getCategoryName().isEmpty()) {
                APIResponse<Object> response = APIResponse.error("Please provide a category name!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            Category category = Category.builder().categoryName(categoryCreateDTO.getCategoryName()).build();
            categoryRepository.saveAndFlush(category);
            CategoryViewDTO viewDTO = CategoryMapper.viewMapper(category);
            APIResponse<CategoryViewDTO> response = APIResponse.successWithData(viewDTO, "Category of the given name is created successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataIntegrityViolationException e) {
            APIResponse<Object> response = APIResponse.error("Category of the given name `%s` is already exists!".formatted(categoryCreateDTO.getCategoryName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> updateCategory(String categoryId, CategoryUpdateDTO categoryUpdateDTO) {
        try {
            UUID uuid = UUID.fromString(categoryId);
            Category category = categoryRepository.findById(uuid)
                    .orElseThrow(() -> new CategoryNotFoundException("Category of the given id not found!"));
            if (categoryUpdateDTO.getNewCategoryName().isEmpty()) {
                APIResponse<Object> response = APIResponse.error("Please provide a valid category name!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } else if (category.getCategoryName().equals(categoryUpdateDTO.getNewCategoryName())) {
                APIResponse<Object> response = APIResponse.error("Old name and the new name of the category cannot be the same!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            category.setCategoryName(categoryUpdateDTO.getNewCategoryName());
            categoryRepository.saveAndFlush(category);
            CategoryViewDTO viewDTO = CategoryMapper.viewMapper(category);
            APIResponse<CategoryViewDTO> response = APIResponse.successWithData(viewDTO, "Category of the given id is updated successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            APIResponse<Object> response = APIResponse.error("Please enter a valid category id!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (CategoryNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (DataIntegrityViolationException e) {
            APIResponse<Object> response = APIResponse.error("Category of the given name `%s` is already exists!".formatted(categoryUpdateDTO.getNewCategoryName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> deleteCategory(String categoryId) {
        try {
            UUID uuid = UUID.fromString(categoryId);
            Category category = categoryRepository.findById(uuid)
                    .orElseThrow(() -> new CategoryNotFoundException("Category of the given id not found!"));
            categoryRepository.delete(category);
            APIResponse<Object> response = APIResponse.success("Category of the given id is deleted successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (CategoryNotFoundException e) {
            APIResponse<Object> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            APIResponse<Object> response = APIResponse.error("Please enter a valid category id!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            APIResponse<Object> response = APIResponse.error("Some error occurred! Please contact IT!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
