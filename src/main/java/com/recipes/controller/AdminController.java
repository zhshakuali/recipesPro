package com.recipes.controller;

import com.recipes.entity.Recipe;
import com.recipes.entity.UserEntity;
import com.recipes.repository.RecipeRepository;
import com.recipes.service.RecipeService;
import com.recipes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final RecipeService recipeService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/admin")
    public String admin(
            @RequestParam("id") Integer id,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
            Model model
    ) {
        if (userService.findRolesById(id).contains("ADMIN")) {
            Page<UserEntity> userEntityList = userService.findAllByUsernameNotLike("admin", page, size);
            Page<Recipe> recipes = recipeService.findAll(page, size);
            model.addAttribute("userId", id);
            model.addAttribute("users", userEntityList);
            model.addAttribute("recipes", recipes);
            return "successadmin";
        } else {
            UserEntity userEntity = userService.findById(id);
            Page<Recipe> recipes = recipeService.findAllByUploaderUsername(userEntity.getUsername(), page, size);

            model.addAttribute("userId", userEntity.getId());
            model.addAttribute("username", userEntity.getUsername());
            model.addAttribute("roles", userEntity.getRoles());
            model.addAttribute("id", userEntity.getId());
            model.addAttribute("recipes", recipes);

            return "successuser";
        }
    }

    @GetMapping("/admin/users")
    public String showUsers(
            @RequestParam("id") Integer id,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
            Model model
    ) {
        Page<UserEntity> userEntityList = userService.findAllByUsernameNotLike("admin", page, size);
        model.addAttribute("userId", id);
        model.addAttribute("users", userEntityList);
        return admin(id, page, size, model);
    }

    @PutMapping("/users/{id}")
    public String updateUser(@PathVariable(value = "id") Integer id, @RequestParam("userId") String adminId, Model model) {
        UserEntity user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("userId", adminId);
        return "updateUser";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable(value = "id") Integer id, @RequestParam("userId") Integer adminId) {
        this.userService.deleteUserById(id);
        return "redirect:/admin?id=" + adminId;
    }

    @GetMapping("/admin/add/user")
    public String showNewEmployeeForm(Model model, @RequestParam("userId") Integer adminId) {
        UserEntity userEntity = new UserEntity();
        model.addAttribute("user", userEntity);
        model.addAttribute("userId", adminId);
        return "addUser";
    }

    @PostMapping("/admin/add")
    public String saveUser(@ModelAttribute("user") UserEntity userEntity, @RequestParam("userId") Integer adminId, Model model) {
        userEntity.setActive(true);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userService.save(userEntity);
        return "redirect:/admin?id=" + adminId;
    }

    @PostMapping("/admin/add/recipe")
    public String saveRecipe(@ModelAttribute("recipe") Recipe recipe, @RequestParam("userId") Integer adminId, Model model) {
        recipe.setDate(LocalDateTime.now());
        Recipe recipeInDb = recipeService.findById(recipe.getId());
        recipe.setUploaderUsername(recipeInDb.getUploaderUsername());
        recipe.setUserEntity(recipeInDb.getUserEntity());
        recipeService.save(recipe);
        return "redirect:/admin?id=" + adminId;
    }

    @PutMapping("/recipes/{id}")
    public String updateRecipe(@PathVariable(value = "id") Integer id, @RequestParam("userId") Integer userId, Model model) {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute("recipe", recipe);
        return "updateRecipe";
    }

    @DeleteMapping("/recipes/{id}")
    public String deleteRecipe(@PathVariable(value = "id") Integer id, @RequestParam("userId") Integer adminId, Model model) {
        this.recipeService.deleteById(id);
        return "redirect:/admin?id=" + adminId;
    }

    @GetMapping("/admin/recipes")
    public String getRecipes(
            Model model,
            @RequestParam("id") Integer id,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Page<Recipe> recipes = recipeService.findAll(page, size);
        model.addAttribute("userId", id);
        model.addAttribute("recipes", recipes);
        return admin(id, page, size, model);
    }
}
