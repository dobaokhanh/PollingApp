package com.poll.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poll.entity.ChoiceVoteCount;
import com.poll.entity.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

	@Query("SELECT NEW com.poll.entity.ChoiceVoteCount(v.choice.id, count(v.id)) FROM Vote v WHERE v.poll.id in :pollIds GROUP BY v.choice.id")
	List<ChoiceVoteCount> countByPollIdGroupByChoiceId(@Param("pollIds") List<Long> pollIds);

	@Query("SELECT NEW com.poll.entity.ChoiceVoteCount(v.choice.id, count(v.id)) FROM Vote v WHERE v.poll.id = : pollId GROUP BY v.choice")
	List<ChoiceVoteCount> countByPollIdGroupByChoiceId(@Param("pollId") Long pollId);
	
	@Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND v.poll.id IN :pollIds")
	List<Vote> findByUserIdAndPollIdIn(@Param("userId") Long userId, @Param("pollIds") List<Long> pollIds);
	
	@Query("SELECT COUNT(v.id) FROM Vote v WHERE v.user.id = :userId")
	long countByUserId(@Param("userId") Long userId);
	
	@Query("SELECT v.poll.id FROM Vote v WHERE v.user.id = :userId")
	Page<Long> findVotedPollByUserId(@Param("userId") Long userId, Pageable pageable);
		
}