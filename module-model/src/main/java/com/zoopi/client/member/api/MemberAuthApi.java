package com.zoopi.client.member.api;

import static com.zoopi.client.member.model.MemberAuthDto.*;

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

import com.zoopi.ErrorResponse;
import com.zoopi.ResultResponse;
import com.zoopi.Void;
import com.zoopi.client.member.model.SigninResponse;
import com.zoopi.client.phoneauthentication.model.PhoneAuthenticationResponse;

/**
 * <b>Swagger Annotation 사용 가이드</b><br>
 * 1. <b>@Api(tags = API 분류)</b>는 interface 단위에 추가한다.<br>
 * <br>
 *
 * 2. <b>@ApiOperation(value = API 명)</b>은 method 단위에 추가한다.<br>
 * <br>
 *
 * 3. <b>@ApiResponse</b> 작성 가이드<br>
 * &nbsp; - <b>200</b><br>
 * &nbsp;&nbsp; - code는 201부터 1씩 증가시키며 작성한다. <br>
 * &nbsp;&nbsp; - message는 ResultCode의 status, code, message를 작성한다. <br>
 * &nbsp;&nbsp; - response는 응답 모델 클래스를 작성한다. <br>
 * &nbsp;&nbsp; ※ 단, response 클래스가 동일한 경우, 하나의 <b>@ApiResponse</b>의 message에 추가로 작성한다. <br>
 * &nbsp;&nbsp; ※ 단, data에 어떤 값도 넣지 않을 경우, response에 zoopi.Void.class를 작성한다. <br>
 * &nbsp; - <b>400</b>: 여기에 작성하지 않고, <b>@ApiOperation(notes = "")</b>에 예외가 발생하지 않게 API를 호출하는 시나리오를 작성한다.<br>
 * &nbsp; - <b>500</b>: 공통적으로 아래 코드를 사용한다.<br>
 * &nbsp;&nbsp; @ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error")<br>
 * <br>
 *
 * 4. <b>@ApiImplicitParam</b>은 메소드 단위에 추가한다.<br>
 * &nbsp; - <b>name</b>: 파라미터의 변수명을 작성한다.<br>
 * &nbsp; - <b>value</b>: 파라미터 설명을 작성한다. Validation이 적용된 경우 괄호로 추가 정보를 작성한다.<br>
 * &nbsp; - <b>required</b>: 파라미터가 필수인 경우만 true로 추가한다. (default: false)<br>
 * &nbsp; - <b>example</b>: 예시 입력 데이터를 작성한다.<br>
 * <br>
 *
 * 5. <b>@ApiModelProperty</b>는 멤버 변수 단위에 추가한다.<br>
 * &nbsp; - <b>name</b>: 사용하지 않는다. (자동 적용)<br>
 * &nbsp; - <b>value</b>: 파라미터 설명을 작성한다. Validation이 적용된 경우 괄호로 추가 정보를 작성한다.<br>
 * &nbsp; - <b>required</b>: 파라미터가 필수인 경우만 true로 추가한다. (default: false)<br>
 * &nbsp; - <b>example</b>: 예시 입력 데이터를 작성한다.<br>
 * <br>
 *
 * <b>※ 주의 사항</b><br>
 * - Dto -> Json 변환 과정에서 변수명이 is___ 인 경우, is가 제거되어 전달된다.<br>
 * - 따라서, <b>@ApiModelProperty</b> 작성 시 주의해야 한다. (name에 is를 뺴고 추가해도 제대로 적용이 안됨)<br>
 * - <b>@JsonProperty</b>와 함께 사용하면 된다. (둘 다 is를 빼기)<br>
 *
 * <br>
 * @author seonpilKim
 */
@Api(tags = "회원 인증 API")
@Validated
@RequestMapping("/auth")
public interface MemberAuthApi {

	@ApiOperation(value = "아이디 유효성 검사")
	@ApiResponses({
		@ApiResponse(code = 201, response = ValidationResponse.class, message = ""
			+ "status: 200 | code: R-M001 | message: 사용 가능한 아이디입니다.\n"
			+ "status: 200 | code: R-M002 | message: 존재하는 아이디입니다."),
		@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error")
	})
	@ApiImplicitParam(name = "username", value = "아이디 (정규표현식: ^[A-Za-z0-9]{6,20}$)", required = true, example = "zoopi123")
	@PostMapping("/username/validate")
	ResponseEntity<ResultResponse> validateUsername(@RequestParam @Pattern(regexp = "^[A-Za-z0-9]{6,20}$") String username);

	@ApiOperation(value = "휴대폰 번호 유효성 검사")
	@ApiResponses({
		@ApiResponse(code = 201, response = ValidationResponse.class, message = ""
			+ "status: 200 | code: R-M003 | message: 사용 가능한 휴대폰 번호입니다\n"
			+ "status: 200 | code: R-M004 | message: 존재하는 휴대폰 번호입니다."),
		@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error")
	})
	@ApiImplicitParam(name = "phone", value = "휴대폰 번호 (정규표현식: ^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$)", required = true, example = "01012345678")
	@PostMapping("/phone/validate")
	ResponseEntity<ResultResponse> validatePhone(
		@RequestParam @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$") String phone);

	@ApiOperation(value = "휴대폰 본인 인증 문자 전송")
	@ApiResponses({
		@ApiResponse(code = 201, response = Void.class, message = "status: 200 | code: R-B001 | message: 인증번호 전송이 제한된 휴대폰 번호입니다. 24시간 후 재시도해 주세요."),
		@ApiResponse(code = 202, response = PhoneAuthenticationResponse.class, message = ""
			+ "status: 200 | code: R-A005 | message: 인증 코드 전송에 성공하였습니다."),
		@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error")
	})
	@PostMapping("/phone/send")
	ResponseEntity<ResultResponse> sendAuthenticationCode(@Valid @RequestBody SendAuthCodeRequest request);

	@ApiOperation(value = "인증 코드 확인")
	@ApiResponses({
		@ApiResponse(code = 201, response = ValidationResponse.class, message = ""
			+ "status: 200 | code: R-A001 | message: 만료된 인증 코드입니다.\n"
			+ "status: 200 | code: R-A002 | message: 인증 코드가 일치하지 않습니다.\n"
			+ "status: 200 | code: R-A003 | message: 인증 코드가 일치합니다."),
		@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error")
	})
	@PostMapping("/code/check")
	ResponseEntity<ResultResponse> checkAuthenticationCode(@Valid @RequestBody AuthCodeCheckRequest request);

	@ApiOperation(value = "일반 회원 가입", notes = ""
		+ "API를 호출하기 전, 아래 사항을 먼저 체크해 주세요.<br>"
		+ "1. 비밀번호와 비밀번호 재확인이 일치하는지?<br>"
		+ "2. 사용 가능한 username인지?<br>"
		+ "3. 사용 가능한 phone인지?<br>")
	@ApiResponses({
		@ApiResponse(code = 201, response = ValidationResponse.class, message = ""
			+ "status: 200 | code: R-M002 | message: 존재하는 아이디입니다.\n"
			+ "status: 200 | code: R-M004 | message: 존재하는 휴대폰 번호입니다.\n"
			+ "status: 200 | code: R-A001 | message: 만료된 인증 코드입니다.\n"
			+ "status: 200 | code: R-A002 | message: 인증 코드가 일치하지 않습니다."),
		@ApiResponse(code = 202, response = Void.class, message = "status: 200 | code: R-M005 | message: 회원 가입에 성공하였습니다."),
		@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error")
	})
	@PostMapping("/signup")
	ResponseEntity<ResultResponse> signup(@Valid @RequestBody SignupRequest request);

	@ApiOperation(value = "일반 로그인")
	@ApiResponses({
		@ApiResponse(code = 201, response = SigninResponse.class, message = ""
			+ "status: 200 | code: R-M008 | message: 회원 아이디가 올바르지 않습니다.\n"
			+ "status: 200 | code: R-M007 | message: 회원 비밀번호가 일치하지 않습니다.\n"
			+ "status: 200 | code: R-M006 | message: 로그인에 성공하였습니다."),
		@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error")
	})
	@PostMapping("/signin")
	ResponseEntity<ResultResponse> signin(@Valid @RequestBody SigninRequest request);

	@ApiOperation(value = "아이디 찾기")
	@ApiResponses({
		@ApiResponse(code = 201, response = ValidationResponse.class, message = ""
			+ "status: 200 | code: R-A001 | message: 만료된 인증 코드입니다.\n"
			+ "status: 200 | code: R-A002 | message: 인증 코드가 일치하지 않습니다."),
		@ApiResponse(code = 202, response = UsernameResponse.class, message = "status: 200 | code: R-M009 | message: 회원 아이디 찾기에 성공하였습니다."),
		@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error")
	})
	@PostMapping("/find/username")
	ResponseEntity<ResultResponse> findUsername(@Valid @RequestBody FindUsernameRequest request);

	@ApiOperation(value = "비밀번호 찾기", notes = ""
		+ "API를 호출하기 전, 아래 사항을 먼저 체크해 주세요.<br>"
		+ "1. 비밀번호와 비밀번호 재확인이 일치하는지?<br>"
		+ "2. 존재하는 username인지?<br>")
	@ApiResponses({
		@ApiResponse(code = 201, response = ValidationResponse.class, message = ""
			+ "status: 200 | code: R-M008 | message: 회원 아이디가 올바르지 않습니다.\n"
			+ "status: 200 | code: R-A001 | message: 만료된 인증 코드입니다.\n"
			+ "status: 200 | code: R-A002 | message: 인증 코드가 일치하지 않습니다."),
		@ApiResponse(code = 202, response = Void.class, message = "status: 200 | code: R-M010 | message: 회원 비밀번호 변경에 성공하였습니다."),
		@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error")
	})
	@PostMapping("/find/password")
	ResponseEntity<ResultResponse> findPassword(@Valid @RequestBody FindPasswordRequest request);

}
