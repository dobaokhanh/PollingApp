package com.poll.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poll.entity.User;
import com.poll.exception.ResourceNotFoundException;
import com.poll.payload.PagedResponse;
import com.poll.payload.PollResponse;
import com.poll.payload.UserIdentityAvailability;
import com.poll.payload.UserProfile;
import com.poll.payload.UserSummary;
import com.poll.repository.PollRepository;
import com.poll.repository.UserRepository;
import com.poll.repository.VoteRepository;
import com.poll.security.CurrentUser;
import com.poll.security.UserPrincipal;
import com.poll.service.PollService;
import com.poll.util.AppConstant;

@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PollRepository pollRepository;

	@Autowired
	private VoteRepository voteRepository;

	@Autowired
	private PollService pollService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	/**
	 * Get current User
	 * 
	 * @param currentUser
	 * @return UserSummary of current user
	 */
	@GetMapping("/user/me")
	@PreAuthorize("hasRole('USER')")
	public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
		UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(),
				currentUser.getName());
		return userSummary;
	}

	/**
	 * Check availability of given username
	 * 
	 * @param username
	 * @return UserIdentityAvailability
	 */
	@GetMapping("/user/checkUsernameAvailability")
	public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
		Boolean isAvailable = !userRepository.existsByUsername(username);
		return new UserIdentityAvailability(isAvailable);
	}

	/**
	 * Get User Profile
	 * 
	 * @param username
	 * @return UserProfile
	 */
	@GetMapping("/users/{username}")
	public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		long pollCount = pollRepository.countByCreatedBy(user.getId());
		long voteCount = voteRepository.countByUserId(user.getId());

		UserProfile userProfile = new UserProfile(user.getId(), user.getName(), user.getUsername(), user.getCreatedAt(),
				pollCount, voteCount);
		return userProfile;
	}

	/**
	 * Get Poll created by given username
	 * 
	 * @param username
	 * @param currentUser
	 * @param page
	 * @param size
	 * @return PageResponse
	 */
	@GetMapping("/users/{username}/polls")
	public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int size) {
		return pollService.getPollsCreatedBy(username, currentUser, page, size);
	}

	/**
	 * Get Poll Voted By Given username
	 * 
	 * @param username
	 * @param currentUser
	 * @param page
	 * @param size
	 * @return PageResponse
	 */
	@GetMapping("/users/{username}/votes")
	public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int size) {
		return pollService.getPollsCreatedBy(username, currentUser, page, size);
	}
}
