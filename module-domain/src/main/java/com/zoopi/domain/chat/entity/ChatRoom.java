package com.zoopi.domain.chat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.zoopi.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "chat_room_id")
	private Long id;

	private Long postId;

	private Long bloodDonorId;
	private Boolean alarmFlag;

	@Enumerated(EnumType.STRING)
	private ChatRoomStatus status;

}
