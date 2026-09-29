package com.bloggingplatformapi.controller;

import com.bloggingplatformapi.dto.PostRequest;
import com.bloggingplatformapi.dto.PostResponse;
import com.bloggingplatformapi.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        PostController controller = new PostController(postService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void createPost_shouldReturn201AndLocation() throws Exception {
        UUID id = UUID.randomUUID();
        PostResponse response = new PostResponse();
        response.setId(id);

        when(postService.createPost(any(PostRequest.class))).thenReturn(response);

        mockMvc.perform(post("/posts")
                        .contentType("application/json")
                        .content("""
                                {
                                  "title": "Spring Boot",
                                  "content": "REST API",
                                  "category": "Java",
                                  "tags": ["spring"]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/posts/" + id));
    }

    @Test
    void deletePost_shouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/posts/{id}", id))
                .andExpect(status().isNoContent());

        verify(postService).deletePostById(id);
    }
}
