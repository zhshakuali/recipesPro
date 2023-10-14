package com.recipes.controller;

import com.recipes.entity.UserEntity;
import com.recipes.entity.Recipe;
import com.recipes.entity.Role;
import com.recipes.repository.RecipeRepository;
import com.recipes.repository.UserRepository;
import com.recipes.security.UserPrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserPrincipalDetailsService userPrincipalDetailsService;

    @GetMapping("/login")
    public String registeredUser(@Valid UserEntity userEntity, Model model){
        if (!userRepository.existsByUsername(userEntity.getUsername())) {
            userEntity.setActive(true);
            userEntity.setRoles(Collections.singleton(Role.USER));
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userRepository.save(userEntity);

            model.addAttribute("userId", userEntity.getId());
            model.addAttribute("username", userEntity.getUsername());
            model.addAttribute("roles", userEntity.getRoles());

            return "successuser";
        }

        Optional<UserEntity> userEntity1 = userRepository.findByUsername(userEntity.getUsername());

        UserEntity user = new UserEntity();
        user.setActive(true);
        user.setId(userEntity1.get().getId());
        user.setUsername(userEntity1.get().getUsername());
        user.setPassword(passwordEncoder.encode(userEntity1.get().getPassword()));
        user.setRoles(userEntity1.get().getRoles());

        List<Recipe> recipeList = recipeRepository.findAllByUploaderUsername(user.getUsername());
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("roles", user.getRoles());
        model.addAttribute("recipes", recipeList);
        model.addAttribute("id", user.getId());
        return "successuser";
    }

    @GetMapping("/add/recipe")
    public String addRecipe(Model model, @RequestParam String username){
        model.addAttribute("username", username);
        return "addRecipe";
    }

    @PostMapping("/add/recipe")
    public String postRecipe(@RequestBody MultiValueMap<String, String> data, Model model) {
        Recipe recipe = new Recipe(data.get("username").get(0), data.get("name").get(0), data.get("category").get(0), data.get("description").get(0));
        Optional<UserEntity> user = userRepository.findByUsername(data.get("username").get(0));
        user.ifPresent(recipe::setUserEntity);
        recipeRepository.save(recipe);

        Optional<UserEntity> userEntity1 = userRepository.findByUsername(data.get("username").get(0));
        List<Recipe> recipeList = recipeRepository.findAllByUploaderUsername(data.get("username").get(0));

        model.addAttribute("userId", userEntity1.get().getId());
        model.addAttribute("username", userEntity1.get().getUsername());
        model.addAttribute("roles", userEntity1.get().getRoles());
        model.addAttribute("recipes", recipeList);
        return "successuser";
    }
}
