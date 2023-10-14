package com.recipes.service;

import com.recipes.entity.Recipe;
import com.recipes.entity.UserEntity;
import com.recipes.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecipeService {
    @Autowired //
    RecipeRepository recipeRepository;

    public Recipe save(Recipe recipe){
        Optional<Recipe> recipeSave = Optional.of(recipeRepository.findById(recipe.getId()).get());

        recipeRepository.save(recipeSave.get());
        return recipeSave.get();
    }

    public Recipe findById(Integer id){
        Optional<Recipe> recipe = recipeRepository.findById(id);
        return recipe.get();
    }
    public void deleteRecipeById(Integer id){
        recipeRepository.deleteById(id);
    }

}
