package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.Category;
import com.database.eventmania.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class CategoryService {
    CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public boolean addCategory(String eventId, String categoryName, String categoryDescription, String capacity, String price) throws SQLException {
        return categoryRepository.addCategory(Long.valueOf(eventId), categoryName, categoryDescription, Integer.valueOf(capacity), Integer.valueOf(price));
    }


    public Category getCategoriesByEventId(String eventId) throws SQLException {
        return categoryRepository.getCategoriesByEventId(Long.valueOf(eventId));
    }
}
