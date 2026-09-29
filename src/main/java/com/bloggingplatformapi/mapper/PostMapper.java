package com.bloggingplatformapi.mapper;

import com.bloggingplatformapi.dto.PostRequest;
import com.bloggingplatformapi.dto.PostResponse;
import com.bloggingplatformapi.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toEntity(PostRequest request);

    @Mapping(target = "authorEmail", source = "author.email")
    PostResponse toResponse(Post post);
}
