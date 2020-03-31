package com.poll.payload;

import java.time.Instant;

public class UserProfile {

	private Long id;

	private String username;

	private Instant joinedAt;

	private String pollCount;

	private String voteCount;

	public UserProfile(Long id, String username, Instant joinedAt, String pollCount, String voteCount) {
		super();
		this.id = id;
		this.username = username;
		this.joinedAt = joinedAt;
		this.pollCount = pollCount;
		this.voteCount = voteCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Instant getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(Instant joinedAt) {
		this.joinedAt = joinedAt;
	}

	public String getPollCount() {
		return pollCount;
	}

	public void setPollCount(String pollCount) {
		this.pollCount = pollCount;
	}

	public String getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(String voteCount) {
		this.voteCount = voteCount;
	}

}
