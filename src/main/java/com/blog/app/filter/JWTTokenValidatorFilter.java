package com.blog.app.filter;

import com.blog.app.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;

	/**
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String jwt = request.getHeader(ApplicationConstants.JWT_HEADER);

		if (jwt != null) {
			try {
				Environment env = getEnvironment();
				if (env != null) {
					String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
							ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);

					SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
					if (secretKey != null) {
						Claims claims = Jwts.parser().verifyWith(secretKey)
								.build().parseSignedClaims(jwt).getPayload();

						String username = claims.get("username", String.class);
						String authorities = claims.get("authorities", String.class);


						UserDetails userDetails = userDetailsService.loadUserByUsername(username);

						Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null,
								AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

						SecurityContextHolder.getContext().setAuthentication(auth);
					}
				}
			}catch (Exception e) {
				throw new BadCredentialsException("Invalid JWT token");
			}

		}
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().equals("/authenticate");
	}
}
