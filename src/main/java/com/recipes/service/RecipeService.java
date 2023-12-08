package com.recipes.service;

import com.recipes.entity.Recipe;
import com.recipes.exceptions.RecipeNotFoundException;
import com.recipes.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public void save(String uploaderUsername, String name, String category, String description) {
        Recipe recipe = Recipe.builder()
                .name(name)
                .category(category)
                .description(description)
                .uploaderUsername(uploaderUsername)
                .build();

        recipeRepository.save(recipe);
    }

    public Page<Recipe> findAllByUploaderUsername(String username, Integer offset, Integer size) {
        Pageable pageable = PageRequest.of(offset, size);
        return recipeRepository
                .findAllByUploaderUsername(username, pageable);
    }

    public void save(Recipe recipe) {
        Recipe recipeToSave = recipeRepository.findById(recipe.getId()).orElseThrow(RecipeNotFoundException::new);

        recipeRepository.save(recipeToSave);
    }

    public Recipe findById(Integer id) {
        return recipeRepository
                .findById(id)
                .orElseThrow(RecipeNotFoundException::new);
    }

    public void deleteById(Integer id) {
        recipeRepository.deleteById(id);
    }

    public Page<Recipe> findAll(int offset, int size) {
        Pageable pageRequest = PageRequest.of(offset, size);
        return recipeRepository.findAll(pageRequest);
    }
}
