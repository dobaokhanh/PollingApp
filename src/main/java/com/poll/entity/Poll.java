package com.poll.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.poll.entity.audit.DateAudit;
import com.poll.entity.audit.UserDateAudit;
import com.sun.istack.NotNull;

@Entity
@Table(name = "polls")
public class Poll extends UserDateAudit{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String question;
	
	@OneToMany(mappedBy = "poll",
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER,
			orphanRemoval = true)
	@Size(min = 2)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 30)
	private List<Choice> choices = new ArrayList<>();
	
	@NotNull
	private Instant expirationDateTime;
	
	public void addChoice (Choice choice) {
		choices.add(choice);
		choice.setPoll(this);
	}
	
	public void removeChoice(Choice choice) {
		choices.remove(choice);
		choice.setPoll(null);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<Choice> getChoices() {
		return choices;
	}

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}

	public Instant getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setExpirationDateTime(Instant expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}
	
}
