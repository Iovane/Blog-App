package com.blog.app.controller;

import com.blog.app.constants.AppUserConstants;
import com.blog.app.constants.PostConstants;
import com.blog.app.dto.PostCreateDTO;
import com.blog.app.dto.PostDetailDTO;
import com.blog.app.dto.ResponseDTO;
import com.blog.app.exceptions.PostNotFoundException;
import com.blog.app.service.PostService;
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
public class PostController {

	private final PostService postService;

	@Operation(summary = "Get all posts", description = "Retrieves all posts in the system")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list of posts"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping("/posts")
	public List<PostDetailDTO> getPost() {
		return postService.findAll();
	}

	@Operation(summary = "Get current user's posts", description = "Retrieves all posts created by the currently logged-in user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved user's posts"),
			@ApiResponse(responseCode = "401", description = "Unauthorized access")
	})
	@GetMapping("/my-posts")
	public List<PostDetailDTO> getMyPost(@AuthenticationPrincipal UserDetails userDetails) {
		return postService.findMyPosts(userDetails.getUsername());
	}

	@Operation(summary = "Save a post", description = "Saves a new post", parameters = {
			@Parameter(name = "X-XSRF-TOKEN", description = "CSRF Token", required = true, example = "abc123")
	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Successfully created a new post"),
			@ApiResponse(responseCode = "400", description = "Invalid request data")
	})
	@PostMapping("/save-post")
	public ResponseEntity<ResponseDTO> savePost(@AuthenticationPrincipal UserDetails userDetails,
	                                            @RequestHeader("X-XSRF-TOKEN") String csrfToken,
	                                            @RequestBody PostCreateDTO postCreateDTO) {
		try {
			postService.save(userDetails.getUsername(), postCreateDTO);

		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO(PostConstants.STATUS_500, PostConstants.MESSAGE_500));
		}
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(new ResponseDTO(PostConstants.STATUS_201, PostConstants.MESSAGE_201));
	}


	@DeleteMapping("/posts/delete/{postId}")
	@Operation(summary = "Delete post", description = "Allows the user to delete their own posts with id and admin to delete any post",
			parameters = { @Parameter(name = "X-XSRF-TOKEN", description = "CSRF Token", required = true, example = "abc123")
	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully deleted the post"),
			@ApiResponse(responseCode = "404", description = "Post not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	public ResponseEntity<ResponseDTO> deletePost(@RequestHeader("X-XSRF-TOKEN") String csrfToken,
	                                              @PathVariable Long postId,
	                                              @AuthenticationPrincipal UserDetails userDetails){

		try{
			 postService.deletePost(postId, userDetails.getUsername());

			return ResponseEntity.ok(new ResponseDTO(PostConstants.STATUS_200, PostConstants.MESSAGE_200));

		} catch(PostNotFoundException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseDTO(PostConstants.STATUS_404, PostConstants.MESSAGE_404));

		} catch(AccessDeniedException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseDTO(PostConstants.STATUS_403, PostConstants.MESSAGE_403));

		} catch (Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO(AppUserConstants.STATUS_500, AppUserConstants.MESSAGE_500));
		}

	}
}
