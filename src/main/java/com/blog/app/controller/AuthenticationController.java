package com.blog.app.controller;

import com.blog.app.constants.AuthConstants;
import com.blog.app.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;

	@GetMapping("/authenticate")
	public ResponseEntity<ResponseDTO> authenticate(@AuthenticationPrincipal UserDetails userDetails) {

		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ResponseDTO(AuthConstants.STATUS_500, "Bad Credentials"));
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDTO(AuthConstants.STATUS_200, AuthConstants.MESSAGE_200));
	}

}
