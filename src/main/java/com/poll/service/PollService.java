package com.poll.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.poll.entity.Choice;
import com.poll.entity.ChoiceVoteCount;
import com.poll.entity.Poll;
import com.poll.entity.User;
import com.poll.entity.Vote;
import com.poll.exception.BadRequestException;
import com.poll.exception.ResourceNotFoundException;
import com.poll.payload.PagedResponse;
import com.poll.payload.PollResponse;
import com.poll.payload.VoteRequest;
import com.poll.repository.PollRepository;
import com.poll.repository.UserRepository;
import com.poll.repository.VoteRepository;
import com.poll.security.UserPrincipal;
import com.poll.util.AppConstant;
import com.poll.util.ModelMapper;

public class PollService {

	private PollRepository pollRepository;

	private VoteRepository voteRepository;

	private UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(PollService.class);

	public PagedResponse<PollResponse> getAllPolls(UserPrincipal currentUser, int page, int size) {

		validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "createdAt");
		Page<Poll> polls = pollRepository.findAll(pageable);

		if (polls.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), polls.getNumber(), polls.getSize(),
					polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
		}

		List<Long> pollIds = polls.map(Poll::getId).getContent();
		Map<Long, Long> choiceVoteCountMap = null;
		return null;
	}

	public PollResponse getPollById(Long pollId, UserPrincipal currentUser) {
		Poll poll = pollRepository.findById(pollId)
				.orElseThrow(() -> new ResourceNotFoundException("Poll", "id", pollId));

		List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(pollId);

		Map<Long, Long> choiceVotesMap = votes.stream()
				.collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

		User creator = userRepository.findById(poll.getCreatedBy())
				.orElseThrow(() -> new ResourceNotFoundException("USer", "id", poll.getCreatedBy()));
		Vote userVote = null;
		
		return null;
	}

	public PollResponse castVoteAndGetUpdatedPoll(Long pollId, VoteRequest voteRequest, UserPrincipal currentUser) {
		Poll poll = pollRepository.findById(pollId)
				.orElseThrow(() -> new ResourceNotFoundException("Poll", "id", pollId));

		if (poll.getExpirationDateTime().isBefore(Instant.now())) {
			throw new BadRequestException("Sorry! This Poll has already expired.");
		}

		User user = userRepository.getOne(currentUser.getId());

		Choice selectedChoice = poll.getChoices().stream()
				.filter(choice -> choice.getId().equals(voteRequest.getChoiceId())).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Choice", "id", voteRequest.getChoiceId()));
		Vote vote = new Vote();
		vote.setPoll(poll);
		vote.setUser(user);
		vote.setChoice(selectedChoice);

		try {
			vote = voteRepository.save(vote);
		} catch (DataIntegrityViolationException ex) {
			logger.info("User {} has already voted in Poll {}", currentUser.getId(), pollId);
			throw new BadRequestException("Sorry! You have already cast your vote in this poll.");
		}

		List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(pollId);
		Map<Long, Long> choiceVotesMap = votes.stream()
				.collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

		User creator = userRepository.findById(poll.getCreatedBy())
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", poll.getCreatedBy()));

		return ModelMapper.mapPollToPollResponse(poll, choiceVotesMap, creator, vote.getChoice().getId());
	}

	private void validatePageNumberAndSize(int page, int size) {
		if (page < 0) {
			throw new BadRequestException("Page number cannot be less than 0 .");
		}

		if (size > AppConstant.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page number cannot be greater than " + AppConstant.MAX_PAGE_SIZE);
		}
	}

	private Map<Long, Long> getChoiceVoteCountMap(List<Long> pollIds) {
		List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(pollIds);

		Map<Long, Long> choiceVotesMap = votes.stream()
				.collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));
		return choiceVotesMap;
	}

	private Map<Long, Long> getPollUserVoteMap(UserPrincipal currentUser, List<Long> pollIds) {
		Map<Long, Long> pollUserVoteMap = null;

		if (currentUser != null) {
			List<Vote> userVotes = voteRepository.findByUserIdAndPollIdIn(currentUser.getId(), pollIds);
			pollUserVoteMap = userVotes.stream()
					.collect(Collectors.toMap(vote -> vote.getPoll().getId(), vote -> vote.getChoice().getId()));
		}

		return pollUserVoteMap;
	}

	Map<Long, User> getPollCreatorMap(List<Poll> polls) {
		List<Long> creatorIds = polls.stream().map(Poll::getCreatedBy).distinct().collect(Collectors.toList());

		List<User> creators = userRepository.findByIdIn(creatorIds);

		Map<Long, User> creatorMap = creators.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		return creatorMap;
	}
}
