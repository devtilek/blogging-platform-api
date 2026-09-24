package com.bloggingplatformapi.service;

import com.bloggingplatformapi.dto.PostRequest;
import com.bloggingplatformapi.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PostService {

    PostResponse getPostById(UUID id);

    PostResponse createPost(PostRequest request);

    Page<PostResponse> getAllPosts(Pageable pageable);

    PostResponse updatePost(UUID id, PostRequest request);

    void deletePostById(UUID id);

    Page<PostResponse> searchPosts(String term, Pageable pageable);
}
