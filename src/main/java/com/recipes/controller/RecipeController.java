package com.recipes.controller;

import com.recipes.entity.Recipe;
import com.recipes.entity.UserEntity;
import com.recipes.service.RecipeService;
import com.recipes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final UserService userService;

    @GetMapping("/recipes/add")
    public String addRecipe(Model model, @RequestParam String username) {
        model.addAttribute("username", username);
        return "addRecipe";
    }

    @GetMapping("/recipes")
    public String getRecipes(Model model,
                             @RequestParam String username,
                             @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                             @RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
        UserEntity user = userService.findByUsername(username);
        Page<Recipe> recipes = recipeService.findAllByUploaderUsername(username, page, size);
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("roles", user.getRoles());
        model.addAttribute("id", user.getId());
        model.addAttribute("recipes", recipes);
        model.addAttribute("username", username);
        return "successuser";
    }

    @PostMapping("/recipes")
    public String postRecipe(Model model,
                             @RequestBody MultiValueMap<String, String> data,
                             @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                             @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        String username = data.get("username").get(0);
        String name = data.get("name").get(0);
        String category = data.get("category").get(0);
        String description = data.get("description").get(0);

        recipeService.save(username, name, category, description);

        UserEntity user = userService.findByUsername(username);
        Page<Recipe> recipes = recipeService.findAllByUploaderUsername(username, page, size);

        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("roles", user.getRoles());
        if (recipes.isEmpty()) {
            recipes = new PageImpl<>(new ArrayList<>());
        }
        model.addAttribute("recipes", recipes);
        return "successuser";
    }
}
