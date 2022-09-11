package com.zoopi.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zoopi.domain.chat.entity.ChatMessage;
import com.zoopi.domain.chat.entity.MessageType;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

	@Query("select c from ChatMessage c where c.chatRoom.id = :chatRoomId and c.type = :type")
	Optional<ChatMessage> findByChatRoomIdAndType(Long chatRoomId, MessageType type);
}
