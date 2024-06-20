package com.dollop.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.data.chat.Conversation;

public interface IConverSationRepo extends JpaRepository<Conversation, Long> {

	@Query("SELECT c FROM Conversation c WHERE c.recipientId.userId =:receipentId AND c.senderId.userId=:senderId")
	Conversation checkConverSation(Long receipentId, Long senderId);

}
