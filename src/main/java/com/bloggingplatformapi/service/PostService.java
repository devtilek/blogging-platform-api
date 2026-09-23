package com.bloggingplatformapi.service;

import com.bloggingplatformapi.dto.PostRequest;
import com.bloggingplatformapi.dto.PostResponse;

import java.util.List;
import java.util.UUID;

public interface PostService {

    PostResponse getPostById(UUID id);
    PostResponse createPost(PostRequest request);
    List<PostResponse> getAllPosts();
    PostResponse updatePost(UUID id, PostRequest request);
    void deletePostById(UUID id);
    List<PostResponse> searchPosts(String term);
}
