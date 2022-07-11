package com.zoopi.domain.bloodpost.dto;

import com.zoopi.domain.bloodpost.entity.BloodPost;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class BloodPostDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RegisterRequest {
        @NotNull
        private Long petId;

        @NotBlank
        @Size(max = 100)
        private String title;

        @NotBlank
        @Size(max = 300)
        private String content;

        @NotNull
        @Min(1)
        private int packCount;

        @NotNull
        private Long hospitalId;

        public BloodPost toEntity() {
            return BloodPost.builder()
                    .petId(this.petId)
                    .title(this.title)
                    .content(this.content)
                    .packCount(this.packCount)
                    .hospitalId(this.hospitalId)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateRequest {
        @NotBlank
        @Size(max = 100)
        private String title;

        @NotBlank
        @Size(max = 300)
        private String content;

        @NotNull
        @Min(1)
        private int packCount;

        @NotNull
        private Long hospitalId;
    }

    @Getter
    @AllArgsConstructor
    public static class InfoResponse {
        private final Long id;
        private final Long petId;
        private final String title;
        private final String content;
        private final int packCount;
        private final Long hospitalId;
        private final BloodPost.Status status;
        private final LocalDateTime pullDate;
        private final int pullCount;

        public InfoResponse(BloodPost bloodPost) {
            this.id = bloodPost.getId();
            this.petId = bloodPost.getPetId();
            this.title = bloodPost.getTitle();
            this.content = bloodPost.getContent();
            this.packCount = bloodPost.getPackCount();
            this.hospitalId = bloodPost.getHospitalId();
            this.status = bloodPost.getStatus();
            this.pullDate = bloodPost.getPullDate();
            this.pullCount = bloodPost.getPullCount();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class RegisterResponse {
        private final Long id;

        public RegisterResponse(BloodPost bloodPost) {
            this.id = bloodPost.getId();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class UpdateResponse {
        private final Long id;
        private final String title;
        private final String content;
        private final int packCount;
        private final Long hospitalId;

        public UpdateResponse(BloodPost bloodPost) {
            this.id = bloodPost.getId();
            this.title = bloodPost.getTitle();
            this.content = bloodPost.getContent();
            this.packCount = bloodPost.getPackCount();
            this.hospitalId = bloodPost.getHospitalId();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ExpireResponse {
        private final Long id;

        public ExpireResponse(BloodPost bloodPost) {
            this.id = bloodPost.getId();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class PullResponse {
        private final Long id;
        private final LocalDateTime pullDate;
        private final int pullCount;

        public PullResponse(BloodPost bloodPost) {
            this.id = bloodPost.getId();
            this.pullDate = bloodPost.getPullDate();
            this.pullCount = bloodPost.getPullCount();
        }
    }

}
