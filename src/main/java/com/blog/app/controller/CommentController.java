package com.blog.app.controller;

import com.blog.app.constants.CommentConstants;
import com.blog.app.dto.CommentCreateDTO;
import com.blog.app.dto.CommentDetailDTO;
import com.blog.app.dto.ResponseDTO;
import com.blog.app.exceptions.CommentNotFoundException;
import com.blog.app.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@Operation(summary = "Get current user's comments", description = "Retrieves all comments made by currently logged-in user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved comments"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping("/my-comments")
	public List<CommentDetailDTO> getMyComments(@AuthenticationPrincipal UserDetails userDetails) {
		return commentService.getMyComments(userDetails.getUsername());
	}

	@Operation(summary = "Create a new comment", description = "Creates a new comment for the currently logged-in user with post id", parameters = {
			@Parameter(name = "X-XSRF-TOKEN", description = "CSRF Token", required = true, example = "abc123")
	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Successfully created a new comment"),
			@ApiResponse(responseCode = "400", description = "Invalid request data")
	})
	@PostMapping("/add-comment")
	public ResponseEntity<ResponseDTO> addComment(@RequestHeader("X-XSRF-TOKEN") String csrfToken,
	                                              @RequestParam Long postId,
	                                              @AuthenticationPrincipal UserDetails userDetails,
	                                              CommentCreateDTO commentCreateDTO) {

		try {
			commentService.addComment(postId, userDetails.getUsername(), commentCreateDTO);
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO(CommentConstants.STATUS_500, CommentConstants.MESSAGE_500));
		}

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(new ResponseDTO(CommentConstants.STATUS_201, CommentConstants.MESSAGE_201));
	}


	@DeleteMapping("/post/comments/{commentId}")
	@Operation(summary = "Delete comment", description = "Allows the user to delete their own comment with id and admin to delete any comment",
			parameters = { @Parameter(name = "X-XSRF-TOKEN", description = "CSRF Token", required = true, example = "abc123")
	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully deleted the comment"),
			@ApiResponse(responseCode = "404", description = "Comment not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	public ResponseEntity<ResponseDTO> deleteComment(@RequestHeader("X-XSRF-TOKEN") String csrfToken,
	                                                 @PathVariable Long commentId,
	                                                 @AuthenticationPrincipal UserDetails userDetails) {

		try {
			commentService.deleteComment(commentId, userDetails.getUsername());

			return ResponseEntity.ok(new ResponseDTO(CommentConstants.STATUS_200, CommentConstants.MESSAGE_200));

		} catch (CommentNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseDTO(CommentConstants.STATUS_404, CommentConstants.MESSAGE_404));

		} catch (AccessDeniedException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseDTO(CommentConstants.STATUS_403, CommentConstants.MESSAGE_403));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO(CommentConstants.STATUS_500, CommentConstants.MESSAGE_500));
		}

	}
}
