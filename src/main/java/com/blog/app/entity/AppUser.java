package com.blog.app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class AppUser extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Column(nullable = false, unique = true)
	private String email;

	@OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<Post> posts;

	@OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<Comment> comments;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REMOVE},  fetch = FetchType.EAGER)
	private Set<Authority> authorities;

	public boolean isAdmin() {
		return this.getAuthorities().stream()
				.anyMatch(authority -> authority.getName().equals("ADMIN"));
	}
}
