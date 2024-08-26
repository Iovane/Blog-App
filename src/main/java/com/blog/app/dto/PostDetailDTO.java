package com.blog.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostDetailDTO {

	private Long id;

	private String title;

	private String content;

	private String author;

	private List<CommentDetailDTO> comments;

}
