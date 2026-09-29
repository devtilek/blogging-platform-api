package com.bloggingplatformapi.service;

import com.bloggingplatformapi.dto.PostRequest;
import com.bloggingplatformapi.dto.PostResponse;
import com.bloggingplatformapi.entity.Post;
import com.bloggingplatformapi.entity.Role;
import com.bloggingplatformapi.entity.User;
import com.bloggingplatformapi.exception.PostNotFoundException;
import com.bloggingplatformapi.mapper.PostMapper;
import com.bloggingplatformapi.repository.PostRepository;
import com.bloggingplatformapi.repository.UserRepository;
import com.bloggingplatformapi.service.impl.PostServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @Mock
    private UserRepository userRepository;

    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        postService = new PostServiceImpl(postRepository, postMapper, userRepository);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
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
    }

    @Test
    void getPostById_shouldThrowWhenPostDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(postRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.getPostById(id));
    }

    @Test
    void updatePost_shouldAllowAuthorToUpdate() {
        UUID id = UUID.randomUUID();
        User author = user("user@example.com", Role.USER);

        Post post = new Post();
        post.setId(id);
        post.setAuthor(author);

        PostRequest request = request();
        PostResponse response = new PostResponse();
        response.setId(id);

        authenticateAs(author.getEmail());
        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(postMapper.toResponse(post)).thenReturn(response);

        PostResponse result = postService.updatePost(id, request);

        assertEquals("Updated", post.getTitle());
        assertEquals(id, result.getId());
        verify(postRepository, never()).save(any(Post.class));
    }

    private PostRequest request() {
        PostRequest request = new PostRequest();
        request.setTitle("Updated");
        request.setContent("New content");
        request.setCategory("Java");
        request.setTags(List.of("spring"));
        return request;
    }

    private User user(String email, Role role) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        user.setRole(role);
        return user;
    }

    private void authenticateAs(String email) {
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(email, null)
        );
    }
}
