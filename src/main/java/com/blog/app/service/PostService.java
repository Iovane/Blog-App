package com.blog.app.service;

import com.blog.app.dto.PostCreateDTO;
import com.blog.app.dto.PostDetailDTO;

import java.util.List;

public interface PostService {

	List<PostDetailDTO> findAll();

	List<PostDetailDTO> findMyPosts(String username);

	void save(String username, PostCreateDTO postCreateDTO);

	void deletePost(Long postId, String username) throws Exception;
}
