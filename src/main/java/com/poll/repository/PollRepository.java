package com.poll.repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poll.entity.Poll;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
	Optional<Poll> findById(Long pollId);

	Page<Poll> findCreatedBy(Long userId, Pageable pageable);

	long countedByCreatedBy(Long userId);

	List<Poll> findByIdIn(List<Long> pollIds);

	List<Poll> findByIdIn(List<Long> pollIds, Sort sort);
}
