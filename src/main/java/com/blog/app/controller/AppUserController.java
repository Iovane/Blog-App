package com.blog.app.controller;

import com.blog.app.constants.AppUserConstants;
import com.blog.app.dto.AppUserCreateDTO;
import com.blog.app.dto.AppUserDetailDTO;
import com.blog.app.dto.ResponseDTO;
import com.blog.app.entity.AppUser;
import com.blog.app.exceptions.UserNotFoundException;
import com.blog.app.mapper.AppUserMapper;
import com.blog.app.service.AppUserService;
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
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class AppUserController {

	private final AppUserService appUserService;

	@PostMapping("/register")
	@Operation(summary = "Register a new user", description = "Registers a new user for the platform")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Successfully created a new user"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	public ResponseEntity<ResponseDTO> createUser(@RequestBody AppUserCreateDTO appUserCreateDTO) {

		try {
			appUserService.createUser(appUserCreateDTO);
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO(AppUserConstants.STATUS_500, e.getMessage()));
		}

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(new ResponseDTO(AppUserConstants.STATUS_201, AppUserConstants.MESSAGE_201));
	}

	@GetMapping("/user")
	@Operation(summary = "Finds user by username", description = "Retrieves the user by it's username (only ADMIN)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	public AppUserDetailDTO getUser(@RequestParam String username) {
		try {
			AppUser byUsername = appUserService.findByUsername(username);
			AppUserDetailDTO appUserDetailDTO = AppUserMapper.mapToAppUserDTO(byUsername, new AppUserDetailDTO());

			return appUserDetailDTO;

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, AppUserConstants.MESSAGE_500);
		}
	}

	@DeleteMapping("/user/delete")
	@Operation(summary = "Delete logged-in user", description = "Allows the logged-in user to deleteMyPost their own account", parameters = {
			@Parameter(name = "X-XSRF-TOKEN", description = "CSRF Token", required = true, example = "abc123")
	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully deleted the user"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	public ResponseEntity<ResponseDTO> deleteLoggedInUser(@RequestHeader("X-XSRF-TOKEN") String csrfToken,
	                                                      @AuthenticationPrincipal UserDetails userDetails) {
		try {
			String username = userDetails.getUsername();
			appUserService.deleteUserByUsername(username, userDetails.getUsername());

			return ResponseEntity.ok(new ResponseDTO(AppUserConstants.STATUS_200, "User successfully deleted"));

		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseDTO(AppUserConstants.STATUS_404, AppUserConstants.MESSAGE_404));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO(AppUserConstants.STATUS_500, AppUserConstants.MESSAGE_500));
		}
	}

	@DeleteMapping("/users/delete/{username}")
	@Operation(summary = "Delete user by username (ADMIN)", description = "Allows an admin to delete a user by their username", parameters = {
			@Parameter(name = "X-XSRF-TOKEN", description = "CSRF Token", required = true, example = "abc123")
	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully deleted the user"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	public ResponseEntity<ResponseDTO> deleteUserByUsername(@RequestHeader("X-XSRF-TOKEN") String csrfToken,
	                                                        @PathVariable String username,
	                                                        @AuthenticationPrincipal UserDetails currentUserDetails) {
		try {
			appUserService.deleteUserByUsername(username, currentUserDetails.getUsername());

			return ResponseEntity.ok(new ResponseDTO(AppUserConstants.STATUS_200, "User successfully deleted"));

		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseDTO(AppUserConstants.STATUS_404, AppUserConstants.MESSAGE_404));

		} catch (AccessDeniedException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseDTO(AppUserConstants.STATUS_403, AppUserConstants.MESSAGE_403));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseDTO(AppUserConstants.STATUS_500, AppUserConstants.MESSAGE_500));
		}
	}

}
