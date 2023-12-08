package com.recipes.controller;

import com.recipes.entity.UserEntity;
import com.recipes.entity.Recipe;
import com.recipes.entity.Role;
import com.recipes.repository.RecipeRepository;
import com.recipes.repository.UserRepository;
import com.recipes.service.RecipeService;
import com.recipes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RecipeService recipeService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String registeredUser(@Valid UserEntity userEntity, Model model,
                                 @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                 @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        if (!userService.existsByUsername(userEntity.getUsername())) {
            userEntity.setActive(true);
            userEntity.setRoles(Collections.singleton(Role.USER));
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userService.save(userEntity);
            model.addAttribute("userId", userEntity.getId());
            model.addAttribute("username", userEntity.getUsername());
            model.addAttribute("roles", userEntity.getRoles());

            return "successuser";
        }

        UserEntity userEntity1 = userService.findByUsername(userEntity.getUsername());
        UserEntity user = new UserEntity();
        user.setActive(true);
        user.setId(userEntity1.getId());
        user.setUsername(userEntity1.getUsername());
        user.setPassword(passwordEncoder.encode(userEntity1.getPassword()));
        user.setRoles(userEntity1.getRoles());

        Page<Recipe> recipes = recipeService.findAllByUploaderUsername(user.getUsername(), page, size);
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("roles", user.getRoles());
        model.addAttribute("id", user.getId());
        if (recipes.isEmpty()) {
            recipes = new PageImpl<>(new ArrayList<>());
        }
        model.addAttribute("recipes", recipes);
        return "successuser";
    }
}
