package com.recipes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import lombok.Data;


@Data
@NoArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Integer id;

    @JsonIgnore
    private String uploaderUsername;

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime date;

    @NotBlank
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private UserEntity userEntity;

    public Recipe(String uploaderUsername, String name, String category, String description){
        this.uploaderUsername = uploaderUsername;
        this.name = name;
        this.category = category;
        this.description = description;
    }
}
