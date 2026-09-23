package com.bloggingplatformapi.service.impl;

import com.bloggingplatformapi.dto.PostRequest;
import com.bloggingplatformapi.dto.PostResponse;
import com.bloggingplatformapi.entity.Post;
import com.bloggingplatformapi.exception.PostNotFoundException;
import com.bloggingplatformapi.mapper.PostMapper;
import com.bloggingplatformapi.repository.PostRepository;
import com.bloggingplatformapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public PostResponse getPostById(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found" + id));

        return postMapper.toResponse(post);

    }

    @Override
    public PostResponse createPost(PostRequest request) {
        Post post = postMapper.toEntity(request);

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);

        return postMapper.toResponse(savedPost);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toResponse)
                .toList();
    }

    @Override
    public PostResponse updatePost(UUID id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found" + id));
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());
        post.setTags(request.getTags());

        post.setUpdatedAt(LocalDateTime.now());
        Post updatePost = postRepository.save(post);
        return postMapper.toResponse(updatePost);
    }

    @Override
    public void deletePostById(UUID id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found" + id));
        postRepository.delete(post);
    }

    @Override
    public List<PostResponse> searchPosts(String term) {
        return postRepository.searchPost(term)
                .stream()
                .map(postMapper::toResponse)
                .toList();
    }
}
