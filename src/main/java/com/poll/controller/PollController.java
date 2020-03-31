package com.poll.controller;

import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.poll.entity.Poll;
import com.poll.payload.ApiResponse;
import com.poll.payload.PagedResponse;
import com.poll.payload.PollRequest;
import com.poll.payload.PollResponse;
import com.poll.payload.VoteRequest;
import com.poll.repository.PollRepository;
import com.poll.repository.UserRepository;
import com.poll.repository.VoteRepository;
import com.poll.security.CurrentUser;
import com.poll.security.UserPrincipal;
import com.poll.service.PollService;
import com.poll.util.AppConstant;

@RestController
@RequestMapping("/api/polls")
public class PollController {

	private PollRepository pollRepository;

	private VoteRepository voteRepository;

	private UserRepository userRepository;

	private PollService pollService;

	private static final Logger logger = LoggerFactory.getLogger(PollController.class);

	/**
	 * Get all polls
	 * 
	 * @param currentUser
	 * @param page
	 * @param size
	 * @return PageResponse
	 */
	@GetMapping
	@PreAuthorize("hasRole('USER')")
	public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int size) {
		return pollService.getAllPolls(currentUser, page, size);
	}

	/**
	 * Create poll
	 * 
	 * @param pollRequest
	 * @return ResponseEntity
	 */
	@GetMapping("/{pollId}")
	public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest) {
		Poll poll = pollService.createPoll(pollRequest);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{pollId}").buildAndExpand(poll.getId())
				.toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "Poll Created Successfully"));
	}

	/** Cast Vote
	 * @param currentUser
	 * @param pollId
	 * @param voteRequest
	 * @return PollResponse
	 */
	@PostMapping("/{pollId}/votes")
	@PreAuthorize("hasRole('USER')")
	public PollResponse caseVote(@CurrentUser UserPrincipal currentUser, @PathVariable Long pollId,
			@Valid @RequestBody VoteRequest voteRequest) {
		
		return pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
	}
}
