package com.bloggingplatformapi.repository;

import com.bloggingplatformapi.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query("""
            SELECT p FROM Post p
            WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :term, '%'))
               OR LOWER(p.content) LIKE LOWER(CONCAT('%', :term, '%'))
               OR LOWER(p.category) LIKE LOWER(CONCAT('%', :term, '%'))
            """)
    Page<Post> searchPosts(@Param("term") String term, Pageable pageable);
}
