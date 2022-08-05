package com.zoopi.domain.chat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.zoopi.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

enum MessageType {

	GENERAL

}

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_message_id")
	private Long id;

	private Long chatRoomId;
	private Long senderId;
	private String message;
	private MessageType type;
	private Boolean readFlag;

}