package com.zoopi.domain.certification.service;

import static com.zoopi.domain.certification.entity.BloodDonationType.*;
import static com.zoopi.util.FunctionalUtils.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.zoopi.domain.certification.dto.CertDetailDto;
import com.zoopi.domain.certification.entity.BloodDonationDetail;
import com.zoopi.domain.certification.entity.BloodDonationHistory;
import com.zoopi.domain.certification.repository.BloodDonationDetailRepository;
import com.zoopi.domain.certification.repository.BloodDonationHistoryRepository;
import com.zoopi.domain.chat.entity.ChatMessage;
import com.zoopi.domain.chat.entity.ChatRoom;
import com.zoopi.domain.chat.entity.ChatRoomStatus;
import com.zoopi.domain.chat.entity.MessageType;
import com.zoopi.domain.chat.repository.ChatMessageRepository;
import com.zoopi.domain.hospital.entity.Hospital;
import com.zoopi.domain.pet.entity.Pet;

class CertificationServiceTest {

	private final BloodDonationHistoryRepository historyRepository = mock(BloodDonationHistoryRepository.class);
	private final BloodDonationDetailRepository detailRepository = mock(BloodDonationDetailRepository.class);
	private final ChatMessageRepository chatMessageRepository = mock(ChatMessageRepository.class);

	private final CertificationService certificationService = new CertificationService(
		historyRepository,
		detailRepository,
		chatMessageRepository
	);

	private final Pet donorPet = Pet.builder().build();
	private final Pet receiverPet = Pet.builder().build();
	private final Hospital hospital1 = new Hospital(1L, "충북대학교 동물병원", "충북 청주시 서원구 충대로1, 충북대학교 수의과대학 동물병원");
	private final Hospital hospital2 = new Hospital(2L, "건국대학교 부속 동물병원", "서울 광진구 능동로 120 건국대학교 수의학관");
	private final List<BloodDonationHistory> histories = List.of(
		new BloodDonationHistory(1L, donorPet, "https://smaple.com/1", hospital1, LocalDate.now(), APPOINT),
		new BloodDonationHistory(2L, donorPet, "https://smaple.com/2", hospital2, LocalDate.now(), APPOINT),
		new BloodDonationHistory(3L, donorPet, "https://smaple.com/3", hospital2, LocalDate.now(), GENERAL),
		new BloodDonationHistory(4L, donorPet, "https://smaple.com/4", hospital2, LocalDate.now(), APPOINT),
		new BloodDonationHistory(5L, donorPet, "https://smaple.com/5", hospital1, LocalDate.now(), GENERAL)
	);
	private final List<BloodDonationDetail> details = List.of(
		new BloodDonationDetail(1L, receiverPet, 100L, "", histories.get(0)),
		new BloodDonationDetail(2L, receiverPet, 200L, "", histories.get(1)),
		new BloodDonationDetail(3L, receiverPet, 300L, "", histories.get(3))
	);
	private final ChatRoom chatRoom = new ChatRoom(200L, 2L, donorPet, true, ChatRoomStatus.DONE);
	private final ChatMessage chatMessage = new ChatMessage(1000L, chatRoom, 1L, "헌혈해주셔서 감사합니다 :)", MessageType.THANKS, true);

	@Test
	public void mapHistoryAndDetail_happy_case() {
		// given
		when(detailRepository.findByHistoryIdsIn(mapFrom(histories, BloodDonationHistory::getId))).thenReturn(details);
		when(chatMessageRepository.findByChatRoomIdAndType(100L, MessageType.THANKS)).thenReturn(Optional.empty());
		when(chatMessageRepository.findByChatRoomIdAndType(200L, MessageType.THANKS)).thenReturn(Optional.of(chatMessage));
		when(chatMessageRepository.findByChatRoomIdAndType(300L, MessageType.THANKS)).thenReturn(Optional.empty());

		// when
		final Map<BloodDonationHistory, CertDetailDto> res = certificationService.mapHistoryAndDetail(histories);

		// then
		res.forEach((history, dto) -> {
			if (history.getType().equals(APPOINT)) {
				assertNotNull(dto);
				if (history.getId() == 2) assertEquals(dto.getThanksMessage(), "헌혈해주셔서 감사합니다 :)");
				else assertNull(dto.getThanksMessage());
			}
			else if (history.getType().equals(GENERAL)) assertNull(dto);
		});
	}

}