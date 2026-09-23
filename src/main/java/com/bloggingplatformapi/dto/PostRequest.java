package com.bloggingplatformapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class PostRequest {
    @NotBlank
    @Size(max = 200)
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    @Size(max = 20)
    private String category;
    private List<String> tags;
}
