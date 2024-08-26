package com.blog.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class AppUserDetailDTO {

	private String username;

	private String email;

	@JsonIgnoreProperties("comments")
	private List<PostDetailDTO> posts;

	private List<CommentDetailDTO> comments;
}
