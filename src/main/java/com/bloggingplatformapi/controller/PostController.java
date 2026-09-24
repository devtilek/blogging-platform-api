package com.bloggingplatformapi.controller;

import com.bloggingplatformapi.dto.PostRequest;
import com.bloggingplatformapi.dto.PostResponse;
import com.bloggingplatformapi.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public PostResponse getPostById(@PathVariable UUID id) {
        return postService.getPostById(id);
    }

    @PostMapping
    public PostResponse createPost(@Valid @RequestBody PostRequest request) {
        return postService.createPost(request);
    }

    @PutMapping("/{id}")
    public PostResponse updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody PostRequest request
    ) {
        return postService.updatePost(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable UUID id) {
        postService.deletePostById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<PostResponse> getPosts(
            @RequestParam(required = false) String term,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        if (term != null && !term.isBlank()) {
            return postService.searchPosts(term, pageable);
        }

        return postService.getAllPosts(pageable);
    }
}
