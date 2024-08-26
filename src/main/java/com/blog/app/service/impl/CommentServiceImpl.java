package com.blog.app.service.impl;

import com.blog.app.dto.CommentCreateDTO;
import com.blog.app.dto.CommentDetailDTO;
import com.blog.app.entity.AppUser;
import com.blog.app.entity.Comment;
import com.blog.app.exceptions.CommentNotFoundException;
import com.blog.app.exceptions.UserNotFoundException;
import com.blog.app.mapper.CommentMapper;
import com.blog.app.repository.AppUserRepository;
import com.blog.app.repository.CommentRepository;
import com.blog.app.repository.PostRepository;
import com.blog.app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final AppUserRepository appUserRepository;
	private final PostRepository postRepository;

	/**
	 * @param username
	 * @return
	 */
	@Override
	public List<CommentDetailDTO> getMyComments(String username) {
		List<Comment> commentList = commentRepository.findByAuthorUsername(username);

		return collectToList(commentList);
	}

	@Override
	public void addComment(Long postId, String username, CommentCreateDTO commentCreateDTO) {

		Comment comment = CommentMapper.mapToComment(commentCreateDTO, new Comment());
		comment.setAuthor(appUserRepository.findByUsername(username).orElse(null));
		comment.setPost(postRepository.findById(postId).orElse(null));
		commentRepository.save(comment);

	}

	/**
	 * @param commentId
	 * @param username
	 */
	@Override
	public void deleteComment(Long commentId, String username) {
		AppUser appUser = appUserRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new CommentNotFoundException("Post not found"));

		if (comment.getAuthor().equals(appUser) || appUser.isAdmin()) {
			commentRepository.delete(comment);
		} else throw new AccessDeniedException("");

	}

	private static List<CommentDetailDTO> collectToList(List<Comment> commentList) {
		return commentList.stream()
				.map(comment -> CommentMapper.mapToCommentDetailDTO(comment, new CommentDetailDTO()))
				.toList();
	}


}
