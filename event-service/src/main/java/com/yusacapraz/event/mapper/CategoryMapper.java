package com.yusacapraz.event.mapper;

import com.yusacapraz.event.model.Category;
import com.yusacapraz.event.model.DTOs.CategoryViewDTO;

public class CategoryMapper {
    public static CategoryViewDTO viewMapper(Category category) {
        return CategoryViewDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }
}
