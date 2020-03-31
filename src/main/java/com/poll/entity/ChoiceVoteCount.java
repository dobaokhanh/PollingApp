package com.poll.entity;

public class ChoiceVoteCount {

	private Long choiceId;

	private Long VoteCount;

	public ChoiceVoteCount(Long choiceId, Long voteCount) {
		super();
		this.choiceId = choiceId;
		this.VoteCount = voteCount;
	}

	public Long getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(Long choiceId) {
		this.choiceId = choiceId;
	}

	public Long getVoteCount() {
		return VoteCount;
	}

	public void setVoteCount(Long voteCount) {
		VoteCount = voteCount;
	}

}
