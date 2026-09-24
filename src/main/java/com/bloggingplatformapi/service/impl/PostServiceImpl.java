package com.bloggingplatformapi.service.impl;

import com.bloggingplatformapi.dto.PostRequest;
import com.bloggingplatformapi.dto.PostResponse;
import com.bloggingplatformapi.entity.Post;
import com.bloggingplatformapi.exception.PostNotFoundException;
import com.bloggingplatformapi.mapper.PostMapper;
import com.bloggingplatformapi.repository.PostRepository;
import com.bloggingplatformapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public PostResponse getPostById(UUID id) {
        Post post = findPostById(id);
        return postMapper.toResponse(post);
    }

    @Override
    @Transactional
    public PostResponse createPost(PostRequest request) {
        Post post = postMapper.toEntity(request);
        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);
    }

    @Override
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(postMapper::toResponse);
    }

    @Override
    @Transactional
    public PostResponse updatePost(UUID id, PostRequest request) {
        Post post = findPostById(id);

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());
        post.setTags(request.getTags());

        return postMapper.toResponse(postRepository.save(post));
    }

    @Override
    @Transactional
    public void deletePostById(UUID id) {
        Post post = findPostById(id);
        postRepository.delete(post);
    }

    @Override
    public Page<PostResponse> searchPosts(String term, Pageable pageable) {
        return postRepository.searchPosts(term.trim(), pageable)
                .map(postMapper::toResponse);
    }

    private Post findPostById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post not found: " + id
                ));
    }
}
