package com.zoopi.domain.bloodpost.service;

import com.zoopi.domain.bloodpost.dto.BloodPostDto;
import com.zoopi.domain.bloodpost.entity.BloodPost;
import com.zoopi.domain.bloodpost.repository.BloodPostRepository;
import com.zoopi.domain.bloodpost.repository.BloodPostRepositoryCustom;
import com.zoopi.exception.EntityNotFoundException;
import com.zoopi.exception.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodPostService {

    private final BloodPostRepository bloodPostRepository;
    private final BloodPostRepositoryCustom bloodPostRepositoryCustom;

    @Transactional(readOnly = true)
    public List<BloodPostDto.InfoResponse> selectAllBloodPost(BloodPost.Status status, String title) {
        return bloodPostRepositoryCustom.searchOrderByPullDateDesc(status, title).stream()
                .map(bloodPost -> new BloodPostDto.InfoResponse(bloodPost))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BloodPostDto.InfoResponse selectBloodPost(Long bloodPostId) {
        return new BloodPostDto.InfoResponse(getBloodPost(bloodPostId));
    }

    @Transactional
    public BloodPostDto.RegisterResponse registerBloodPost(BloodPost bloodPost) {
        return new BloodPostDto.RegisterResponse(bloodPostRepository.save(bloodPost));
    }

    @Transactional
    public BloodPostDto.UpdateResponse updateBloodPost(Long bloodPostId, BloodPostDto.UpdateRequest request) {
        BloodPost bloodPost = getBloodPost(bloodPostId);

        bloodPost.update(request);

        return new BloodPostDto.UpdateResponse(bloodPost);
    }

    @Transactional
    public void deleteBloodPost(Long bloodPostId) {
        bloodPostRepository.deleteById(bloodPostId);
    }

    @Transactional
    public BloodPostDto.ExpireResponse expireBloodPost(Long bloodPostId) {
        BloodPost bloodPost = getBloodPost(bloodPostId);

        bloodPost.expire();

        return new BloodPostDto.ExpireResponse(bloodPost);
    }

    @Transactional
    public BloodPostDto.PullResponse pullBloodPost(Long bloodPostId) {
        BloodPost bloodPost = getBloodPost(bloodPostId);

        bloodPost.pull();

        return new BloodPostDto.PullResponse(bloodPost);
    }

    private BloodPost getBloodPost(Long bloodPostId) {
        return bloodPostRepository.findById(bloodPostId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.BLOODPOST_NOT_FOUND));
    }

}
