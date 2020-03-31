package com.poll.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poll.repository.PollRepository;
import com.poll.repository.UserRepository;
import com.poll.repository.VoteRepository;
import com.poll.service.PollService;

@RestController
@RequestMapping("/api/polls")
public class PollController {
	
	private PollRepository pollRepository;
	
	private VoteRepository voteRepository;
	
	private UserRepository userRepository;
	
	private PollService pollService;
}
