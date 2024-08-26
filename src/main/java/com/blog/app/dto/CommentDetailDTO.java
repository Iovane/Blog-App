package com.blog.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CommentDetailDTO {

	private Long id;

	private String postTitle;

	private String commentContent;

	private String author;

}
