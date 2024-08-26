package com.blog.app.mapper;

import com.blog.app.dto.CommentCreateDTO;
import com.blog.app.dto.CommentDetailDTO;
import com.blog.app.entity.Comment;

import java.util.List;

public class CommentMapper {

	public static CommentDetailDTO mapToCommentDetailDTO(Comment comment, CommentDetailDTO commentDetailDTO) {
		commentDetailDTO.setId(comment.getId());
		commentDetailDTO.setPostTitle(comment.getPost().getTitle());
		commentDetailDTO.setCommentContent(comment.getContent());
		commentDetailDTO.setAuthor(comment.getAuthor().getUsername());

		return commentDetailDTO;
	}

	public static List<CommentDetailDTO> mapToListOfCommentDetailDTO(List<Comment> comments) {
		return comments.stream()
				.map(comment -> CommentMapper.mapToCommentDetailDTO(comment, new CommentDetailDTO()))
				.toList();
	}

	public static Comment mapToComment(CommentCreateDTO commentCreateDTO, Comment comment) {
		comment.setContent(commentCreateDTO.getContent());

		return comment;
	}
}
