package bk.edu.repository;

import bk.edu.data.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    CategoryEntity findByCategoryId(int id);

    Page<CategoryEntity> findAll(Pageable pageable);

    List<CategoryEntity> findAll();
}
