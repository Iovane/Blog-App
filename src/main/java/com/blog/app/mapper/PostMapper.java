package com.blog.app.mapper;

import com.blog.app.dto.PostCreateDTO;
import com.blog.app.dto.PostDetailDTO;
import com.blog.app.entity.Post;

import java.util.List;

public class PostMapper {

	public static PostDetailDTO mapToPostDetailDTO(Post post, PostDetailDTO postDetailDTO) {
		postDetailDTO.setId(post.getId());
		postDetailDTO.setTitle(post.getTitle());
		postDetailDTO.setContent(post.getContent());
		postDetailDTO.setAuthor(post.getAuthor().getUsername());
		postDetailDTO.setComments(CommentMapper.mapToListOfCommentDetailDTO(post.getComments()));

		return postDetailDTO;
	}

	public static List<PostDetailDTO> mapToListOfPostDetailDTO(List<Post> posts) {
		return posts.stream()
				.map(post -> PostMapper.mapToPostDetailDTO(post, new PostDetailDTO()))
				.toList();
	}

	public static Post mapToPost(PostCreateDTO postCreateDTO, Post post) {
		post.setTitle(postCreateDTO.getTitle());
		post.setContent(postCreateDTO.getContent());

		return post;
	}

}
