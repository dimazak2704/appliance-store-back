package com.epam.dimazak.appliances.repository;

import com.epam.dimazak.appliances.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
