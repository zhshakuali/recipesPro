package com.recipes.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.recipes.entity.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc(String name);

    List<Recipe> findAllByUploaderUsername(String uploaderUsername);

    @Query(value = "SELECT * FROM recipes", nativeQuery = true)
    List<Recipe> findAll();
}