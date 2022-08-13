package com.zoopi.api.client.member;

import static com.zoopi.model.client.member.MemberAuthDto.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.zoopi.model.ResultResponse;

@Api(tags = "회원 인증 API")
@Validated
@RequestMapping("/auth")
public interface MemberAuthApi {

	@ApiOperation(value = "아이디 유효성 검사")
	@ApiResponses({
		@ApiResponse(code = 200, message = "[R-M001] 사용 가능한 아이디입니다.", response = ValidationResponse.class)
	})
	@ApiImplicitParam(name = "username", value = "아이디", required = true, example = "zoopi")
	@PostMapping("/username/validate")
	ResponseEntity<ResultResponse> validateUsername(@RequestParam @Size(max = 30) String username);

	@ApiOperation(value = "휴대폰 번호 유효성 검사")
	@ApiImplicitParam(name = "phone", value = "휴대폰 번호", required = true, example = "01012345678")
	@PostMapping("/phone/validate")
	ResponseEntity<ResultResponse> validatePhone(
		@RequestParam @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$") String phone);

	@ApiOperation(value = "휴대폰 본인 인증 문자 전송")
	@PostMapping("/phone/send")
	ResponseEntity<ResultResponse> sendAuthenticationCode(@Valid @RequestBody SendAuthCodeRequest request);

	@ApiOperation(value = "인증 코드 확인")
	@PostMapping("/code/check")
	ResponseEntity<ResultResponse> checkAuthenticationCode(@Valid @RequestBody AuthCodeCheckRequest request);

	@ApiOperation(value = "일반 회원 가입")
	@PostMapping("/signup")
	ResponseEntity<ResultResponse> signup(@Valid @RequestBody SignupRequest request);

	@ApiOperation(value = "일반 로그인")
	@PostMapping("/signin")
	ResponseEntity<ResultResponse> signin(@Valid @RequestBody SigninRequest request);

	@ApiOperation(value = "아이디 찾기")
	@PostMapping("/find/username")
	ResponseEntity<ResultResponse> findUsername(@Valid @RequestBody FindUsernameRequest request);

	@ApiOperation(value = "비밀번호 찾기")
	@PostMapping("/find/password")
	ResponseEntity<ResultResponse> findPassword(@Valid @RequestBody FindPasswordRequest request);

}
