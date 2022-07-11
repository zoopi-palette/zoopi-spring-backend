package com.zoopi.controller.bloodpost;

import com.zoopi.controller.ResultCode;
import com.zoopi.controller.ResultResponse;
import com.zoopi.domain.bloodpost.dto.BloodPostDto;
import com.zoopi.domain.bloodpost.entity.BloodPost;
import com.zoopi.domain.bloodpost.service.BloodPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "헌혈 요청글 API")
@RestController
@RequestMapping("/blood-post")
@RequiredArgsConstructor
public class BloodPostController {

    private final BloodPostService bloodPostService;

    @ApiOperation("헌혈 요청글 리스트 조회")
    @GetMapping
    public ResponseEntity<ResultResponse> selectAllBloodPost(
            @RequestParam(value = "status", required = false) BloodPost.Status status,
            @RequestParam(value = "title", required = false) String title
    ) {
        List<BloodPostDto.InfoResponse> response = bloodPostService.selectAllBloodPost(status, title);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.BLOODPOST_SELECTALL_SUCCESS, response));
    }

    @ApiOperation("헌혈 요청글 상세조회")
    @GetMapping("/{bloodPostId}")
    public ResponseEntity<ResultResponse> selectBloodPost(
            @PathVariable Long bloodPostId
    ) {
        BloodPostDto.InfoResponse response = bloodPostService.selectBloodPost(bloodPostId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.BLOODPOST_SELECT_SUCCESS, response));
    }

    @ApiOperation("헌혈 요청글 등록")
    @PostMapping
    public ResponseEntity<ResultResponse> registerBloodPost(
            @RequestBody @Valid BloodPostDto.RegisterRequest request
    ) {
        BloodPostDto.RegisterResponse response = bloodPostService.registerBloodPost(request.toEntity());

        return ResponseEntity.ok(ResultResponse.of(ResultCode.BLOODPOST_REGISTER_SUCCESS, response));
    }

    @ApiOperation("헌혈 요청글 수정")
    @PatchMapping("/{bloodPostId}")
    public ResponseEntity<ResultResponse> updateBloodPost(
            @PathVariable Long bloodPostId,
            @RequestBody @Valid BloodPostDto.UpdateRequest request
    ) {
        BloodPostDto.UpdateResponse response = bloodPostService.updateBloodPost(bloodPostId, request);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.BLOODPOST_UPDATE_SUCCESS, response));
    }

    @ApiOperation("헌혈 요청글 삭제")
    @DeleteMapping("/{bloodPostId}")
    public ResponseEntity<ResultResponse> deleteBloodPost(
            @PathVariable Long bloodPostId
    ) {
        bloodPostService.deleteBloodPost(bloodPostId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.BLOODPOST_DELETE_SUCCESS));
    }

    @ApiOperation("헌혈 요청글 만료")
    @PatchMapping("/expire/{bloodPostId}")
    public ResponseEntity<ResultResponse> expireBloodPost(
            @PathVariable Long bloodPostId
    ) {
        BloodPostDto.ExpireResponse response = bloodPostService.expireBloodPost(bloodPostId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.BLOODPOST_EXPIRE_SUCCESS, response));
    }

    @ApiOperation("헌혈 요청글 끌어올리기")
    @PatchMapping("/pull/{bloodPostId}")
    public ResponseEntity<ResultResponse> pullBloodPost(
            @PathVariable Long bloodPostId
    ) {
        BloodPostDto.PullResponse response = bloodPostService.pullBloodPost(bloodPostId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.BLOODPOST_EXPIRE_SUCCESS, response));
    }

}
