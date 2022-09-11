package com.zoopi.domain.certification.service;

import static com.zoopi.util.FunctionalUtils.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.zoopi.domain.certification.dto.CertDetailDto;
import com.zoopi.domain.certification.entity.BloodDonationDetail;
import com.zoopi.domain.certification.entity.BloodDonationHistory;
import com.zoopi.domain.certification.repository.BloodDonationDetailRepository;
import com.zoopi.domain.certification.repository.BloodDonationHistoryRepository;
import com.zoopi.domain.chat.entity.ChatMessage;
import com.zoopi.domain.chat.entity.MessageType;
import com.zoopi.domain.chat.repository.ChatMessageRepository;

@Service
@RequiredArgsConstructor
public class CertificationService {

	private final BloodDonationHistoryRepository historyRepository;
	private final BloodDonationDetailRepository detailRepository;
	private final ChatMessageRepository chatMessageRepository;

	public List<BloodDonationHistory> findAllHistoryBy(Long petId) {
		return historyRepository.findAllByPetId(petId);
	}

	@Transactional
	public Map<BloodDonationHistory, CertDetailDto> mapHistoryAndDetail(List<BloodDonationHistory> histories) {
		final List<Long> ids = mapFrom(histories, BloodDonationHistory::getId);
		final List<BloodDonationDetail> details = detailRepository.findByHistoryIdsIn(ids);
		final Map<Long, CertDetailDto> certDtoMap =
			associateFrom(details, detail -> detail.getHistory().getId(), this::makeCertDetailDto);

		return associateFrom(histories, history -> history, history -> certDtoMap.get(history.getId()));
	}

	public CertDetailDto makeCertDetailDto(BloodDonationDetail detail) {
		ChatMessage thanksMessage = chatMessageRepository.findByChatRoomIdAndType(detail.getChatRoomId(), MessageType.THANKS);
		return new CertDetailDto(detail, thanksMessage);
	}

}
