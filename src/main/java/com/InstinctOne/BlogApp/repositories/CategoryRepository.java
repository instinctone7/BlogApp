package com.InstinctOne.BlogApp.repositories;

import com.InstinctOne.BlogApp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
}
