package com.recipes.controller;

import com.recipes.entity.Recipe;
import com.recipes.entity.UserEntity;
import com.recipes.repository.RecipeRepository;
import com.recipes.repository.UserRepository;
import com.recipes.service.RecipeService;
import com.recipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    RecipeService recipeService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/admin")
    public String admin(@RequestParam("id") Integer id, Model model){
        if(userRepository.findRolesById(id).equals("ADMIN")){
            List<UserEntity> userEntityList = userRepository.findAll();
            List<Recipe>  recipeList = recipeRepository.findAll();
            model.addAttribute("userId", id);
            model.addAttribute("users", userEntityList);
            model.addAttribute("recipes", recipeList);
            return "successadmin";
        } else {
            Optional<UserEntity> userEntity = userRepository.findById(id);
            List<Recipe> recipeList = recipeRepository.findAllByUploaderUsername(userEntity.get().getUsername());

            model.addAttribute("userId", userEntity.get().getId());
            model.addAttribute("username", userEntity.get().getUsername());
            model.addAttribute("roles", userEntity.get().getRoles());
            model.addAttribute("recipes", recipeList);
            model.addAttribute("id", userEntity.get().getId());

            return "successuser";
        }
    }

    @GetMapping("/updateUser/{id}")
    public String updateUser(@PathVariable(value="id") Integer id, Model model){
        UserEntity user = userService.findById(id);
        model.addAttribute("user", user);
        return "updateUser";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable(value="id") Integer id){
        this.userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/add/user")
    public String showNewEmployeeForm(Model model) {
        UserEntity userEntity = new UserEntity();
        model.addAttribute("user", userEntity);
        return "addUser";
    }

    @PostMapping("/admin/add")
    public String saveUser(@ModelAttribute("user") UserEntity userEntity, Model model){
        userEntity.setActive(true);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        return "redirect:/admin";
    }

    @PostMapping("/admin/add/recipe")
    public String saveRecipe(@ModelAttribute("recipe") Recipe recipe, Model model){
        recipe.setDate(LocalDateTime.now());
        recipe.setUploaderUsername(recipeRepository.findById(recipe.getId()).get().getUploaderUsername());
        recipe.setUserEntity(recipeRepository.findById(recipe.getId()).get().getUserEntity());
        recipeRepository.save(recipe);
        return "redirect:/admin";
    }

    @GetMapping("/updateRecipe/{id}")
    public String updateRecipe(@PathVariable(value="id") Integer id, Model model){
        Recipe recipe = recipeService.findById(id);
        model.addAttribute("recipe", recipe);
        return "updateRecipe";
    }

    @PostMapping("/deleteRecipe/{id}")
    public String delete(@PathVariable(value="id") Integer id){
        this.recipeService.deleteRecipeById(id);
        return "redirect:/admin";
    }

    @GetMapping("/deleteRecipe/{id}")
    public String deleteRecipe(@PathVariable(value = "id") Integer id){
        this.recipeRepository.deleteById(id);
        return "redirect:/admin";
    }
}
