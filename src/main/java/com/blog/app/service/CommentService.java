package com.blog.app.service;

import com.blog.app.dto.CommentCreateDTO;
import com.blog.app.dto.CommentDetailDTO;

import java.util.List;

public interface CommentService {

	List<CommentDetailDTO> getMyComments(String username);

	void addComment(Long postId, String username, CommentCreateDTO commentCreateDTO);

	void deleteComment(Long commentId, String username);
}
