package com.blog.app.service.impl;

import com.blog.app.dto.PostCreateDTO;
import com.blog.app.dto.PostDetailDTO;
import com.blog.app.entity.AppUser;
import com.blog.app.entity.Post;
import com.blog.app.exceptions.PostNotFoundException;
import com.blog.app.exceptions.UserNotFoundException;
import com.blog.app.mapper.PostMapper;
import com.blog.app.repository.AppUserRepository;
import com.blog.app.repository.PostRepository;
import com.blog.app.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final AppUserRepository appUserRepository;

	/**
	 * @return
	 */
	@Override
	public List<PostDetailDTO> findAll() {
		List<Post> allPosts = postRepository.findAll();

		return collectToList(allPosts);
	}

	@Override
	public List<PostDetailDTO> findMyPosts(String username) {
		List<Post> byAuthorUsername = postRepository.findByAuthorUsername(username);

		return collectToList(byAuthorUsername);
	}

	@Override
	public void save(String username, PostCreateDTO postCreateDTO) {
		Post post = PostMapper.mapToPost(postCreateDTO, new Post());
		post.setAuthor(appUserRepository.findByUsername(username).orElse(null));

		postRepository.save(post);
	}

	@Override
	public void deletePost(Long postId, String username) throws Exception {
		AppUser appUser = appUserRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post not found"));

		if (post.getAuthor().equals(appUser) || appUser.isAdmin()) {
			postRepository.delete(post);
		} else throw new AccessDeniedException("");
	}


	private static List<PostDetailDTO> collectToList(List<Post> postsList) {
		return postsList.stream()
				.map(post -> PostMapper.mapToPostDetailDTO(post, new PostDetailDTO()))
				.toList();
	}
}
