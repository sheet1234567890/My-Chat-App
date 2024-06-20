package com.dollop.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.dollop.app.data.chat.ChatMessage;
import com.dollop.app.data.chat.MessageStatus;
import com.dollop.app.payload.ChatMessageDTO;
import com.dollop.app.payload.LastMessageResponse;
import com.dollop.app.util.AppConstant;

public interface IChatMessageRepo extends JpaRepository<ChatMessage, Long>{

	@Query("SELECT c FROM ChatMessage c WHERE c. conversationId=:conversationId")
	List<ChatMessage> findAllMsgByConversationId(String conversationId);

    @Query(value = "SELECT * FROM chat_message as c WHERE c.conversation_id = :conversationId ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
	ChatMessage findLastMessageOfReceiverAndSender(String conversationId);

//    @Query("SELECT new com.dollop.app.payload.ChatMessageDTO(c.receiver.userId, c.status,c.message)FROM ChatMessage c WHERE c.receiver.userId =:userId AND c.status=:status")
//	List<ChatMessageDTO> findAllReceivedMessageByReceiverId(@Param("userId") Long userId,@Param("status") MessageStatus status);

    @Query("SELECT c FROM ChatMessage c WHERE c.receiver.userId=:userId AND c.status=:status")
    List<ChatMessage> findAllReceivedMessageOfReceiver(Long userId,MessageStatus status);

	
}
