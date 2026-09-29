package com.bloggingplatformapi.service;

import com.bloggingplatformapi.dto.PostRequest;
import com.bloggingplatformapi.dto.PostResponse;
import com.bloggingplatformapi.entity.Post;
import com.bloggingplatformapi.exception.PostNotFoundException;
import com.bloggingplatformapi.mapper.PostMapper;
import com.bloggingplatformapi.repository.PostRepository;
import com.bloggingplatformapi.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        postService = new PostServiceImpl(postRepository, postMapper);
    }

    @Test
    void getPostById_shouldReturnPost() {
        UUID id = UUID.randomUUID();
        Post post = new Post();
        post.setId(id);
        PostResponse response = new PostResponse();
        response.setId(id);

        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(postMapper.toResponse(post)).thenReturn(response);

        PostResponse result = postService.getPostById(id);

        assertEquals(id, result.getId());
        verify(postMapper).toResponse(post);
    }

    @Test
    void getPostById_shouldThrowWhenPostDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(postRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getPostById(id));
    }

    @Test
    void updatePost_shouldUpdateManagedEntity() {
        UUID id = UUID.randomUUID();
        Post post = new Post();
        post.setId(id);

        PostRequest request = new PostRequest();
        request.setTitle("Updated");
        request.setContent("New content");
        request.setCategory("Java");
        request.setTags(List.of("spring"));

        PostResponse response = new PostResponse();
        response.setId(id);

        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(postMapper.toResponse(post)).thenReturn(response);

        PostResponse result = postService.updatePost(id, request);

        assertEquals("Updated", post.getTitle());
        assertEquals("New content", post.getContent());
        assertEquals("Java", post.getCategory());
        assertEquals(List.of("spring"), post.getTags());
        assertEquals(id, result.getId());
        verify(postRepository, never()).save(any(Post.class));
    }
}
