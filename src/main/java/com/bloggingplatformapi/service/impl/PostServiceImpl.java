package com.bloggingplatformapi.service.impl;

import com.bloggingplatformapi.dto.PostRequest;
import com.bloggingplatformapi.dto.PostResponse;
import com.bloggingplatformapi.entity.Post;
import com.bloggingplatformapi.entity.Role;
import com.bloggingplatformapi.entity.User;
import com.bloggingplatformapi.exception.ForbiddenException;
import com.bloggingplatformapi.exception.PostNotFoundException;
import com.bloggingplatformapi.mapper.PostMapper;
import com.bloggingplatformapi.repository.PostRepository;
import com.bloggingplatformapi.repository.UserRepository;
import com.bloggingplatformapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    @Override
    public PostResponse getPostById(UUID id) {
        return postMapper.toResponse(findPostById(id));
    }

    @Override
    @Transactional
    public PostResponse createPost(PostRequest request) {
        Post post = postMapper.toEntity(request);
        post.setAuthor(currentUser());

        return postMapper.toResponse(postRepository.save(post));
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
        assertCanModify(post);

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());
        post.setTags(request.getTags());

        return postMapper.toResponse(post);
    }

    @Override
    @Transactional
    public void deletePostById(UUID id) {
        Post post = findPostById(id);
        assertCanModify(post);
        postRepository.delete(post);
    }

    @Override
    public Page<PostResponse> searchPosts(String term, Pageable pageable) {
        return postRepository.searchPosts(term.trim(), pageable)
                .map(postMapper::toResponse);
    }

    private Post findPostById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found: " + id));
    }

    private User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new ForbiddenException("Authentication is required");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ForbiddenException("Authenticated user not found"));
    }

    private void assertCanModify(Post post) {
        User user = currentUser();

        if (user.getRole() == Role.ADMIN) {
            return;
        }

        if (post.getAuthor() == null || !post.getAuthor().getId().equals(user.getId())) {
            throw new ForbiddenException("You can only modify your own posts");
        }
    }
}
