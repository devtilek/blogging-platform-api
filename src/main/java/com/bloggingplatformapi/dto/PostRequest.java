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
    @Size(max = 20_000)
    private String content;

    @NotBlank
    @Size(max = 20)
    private String category;

    @Size(max = 10)
    private List<@NotBlank @Size(max = 50) String> tags;
}
