package com.zoopi.domain.bloodpost.entity;

import com.zoopi.domain.BaseEntity;
import com.zoopi.domain.bloodpost.dto.BloodPostDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "blood_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BloodPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blood_post_id")
    private Long id;

    // TODO: 반려동물 엔티티 구현 시 수정
    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "title", nullable = false)
    @Size(max = 100)
    private String title;

    @Column(name = "content", nullable = false)
    @Size(max = 300)
    private String content;

    @Column(name = "pack_count", nullable = false)
    private int packCount;

    // TODO: 동물병원 데이터 이슈 해결 시 수정
    @Column(name = "hospital_id", nullable = false)
    private Long hospitalId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "pull_date")
    private LocalDateTime pullDate;

    @Column(name = "pull_count", nullable = false)
    private int pullCount;

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        TODO("헌혈요청"),
        PROGRESS("매칭완료"),
        DONE("헌혈완료"),
        EXPIRE("만료");

        private final String description;
    }

    @Builder
    public BloodPost(
            Long petId,
            String title,
            String content,
            int packCount,
            Long hospitalId
    ) {
        this.petId = petId;
        this.title = title;
        this.content = content;
        this.packCount = packCount;
        this.hospitalId = hospitalId;
        this.status = Status.TODO;
        this.pullDate = LocalDateTime.now();
        this.pullCount = 0;
    }

    public void todo() {
        this.status = Status.TODO;
    }

    public void progress() {
        this.status = Status.PROGRESS;
    }

    public void done() {
        this.status = Status.DONE;
    }

    public void expire() {
        this.status = Status.EXPIRE;
    }

    public void pull() {
        this.pullDate = LocalDateTime.now();
        this.pullCount++;
    }

    public void update(BloodPostDto.UpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.packCount = request.getPackCount();
        this.hospitalId = request.getHospitalId();
    }

}
