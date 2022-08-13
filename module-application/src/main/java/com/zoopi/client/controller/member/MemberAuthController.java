package com.zoopi.client.controller.member;

import static com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationConstants.*;
import static com.zoopi.ResultCode.*;
import static com.zoopi.client.model.member.MemberAuthDto.*;
import static com.zoopi.util.Constants.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.zoopi.client.api.member.MemberAuthApi;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationResult;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationType;
import com.zoopi.ResultResponse;
import com.zoopi.client.model.member.SigninResponse;
import com.zoopi.client.model.pheonauthentication.PhoneAuthenticationResponse;
import com.zoopi.client.service.member.MemberService;
import com.zoopi.client.service.phoneauthentication.PhoneAuthenticationBanService;
import com.zoopi.client.service.phoneauthentication.PhoneAuthenticationService;
import com.zoopi.util.AuthenticationCodeUtils;

@RestController
@RequiredArgsConstructor
public class MemberAuthController implements MemberAuthApi {

	private final MemberService memberService;
	private final PhoneAuthenticationService phoneAuthenticationService;
	private final PhoneAuthenticationBanService phoneAuthenticationBanService;

	@Override
	public ResponseEntity<ResultResponse> validateUsername(String username) {
		final boolean isAvailable = memberService.isAvailableUsername(username);
		final ResultResponse response = ValidationResponse.fromValidatingUsername(username, isAvailable);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<ResultResponse> validatePhone(String phone) {
		final boolean isAvailable = memberService.isAvailablePhone(phone);
		final ResultResponse response = ValidationResponse.fromValidatingPhone(phone, isAvailable);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<ResultResponse> sendAuthenticationCode(SendAuthCodeRequest request) {
		// TODO: 만료 인증코드 삭제 -> batch로 변경
		// phoneAuthenticationService.deleteExpiredAuthenticationCodes();

		if (phoneAuthenticationBanService.isBanned(request.getPhone(), request.getType())) {
			return ResponseEntity.ok(ResultResponse.of(PHONE_BANNED));
		}

		final String authenticationCode = AuthenticationCodeUtils.generateRandomAuthenticationCode(
			PHONE_AUTHENTICATION_CODE_LENGTH);
		phoneAuthenticationService.sendAuthenticationCode(request.getPhone(), authenticationCode);

		final PhoneAuthenticationResponse response = phoneAuthenticationService.createAuthentication(request.getPhone(),
			authenticationCode, request.getType());
		if (phoneAuthenticationService.getCountOfAuthentication(request.getPhone(), request.getType())
			>= PHONE_AUTHENTICATION_SEND_MAX_COUNT) {
			phoneAuthenticationBanService.ban(request.getPhone(), request.getType());
		}
		return ResponseEntity.ok(ResultResponse.of(SEND_AUTHENTICATION_CODE_SUCCESS, response));
	}

	@Override
	public ResponseEntity<ResultResponse> checkAuthenticationCode(AuthCodeCheckRequest request) {
		final PhoneAuthenticationResult result = phoneAuthenticationService.checkAuthenticationCode(
			request.getAuthenticationKey(), request.getAuthenticationCode(), request.getPhone(), request.getType());
		final ResultResponse response = ValidationResponse.fromCheckingAuthenticationCode(result, request.getPhone());
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<ResultResponse> signup(SignupRequest request) {
		final PhoneAuthenticationResult result = phoneAuthenticationService
			.validateAuthentication(request.getAuthenticationKey(), request.getPhone(),
				PhoneAuthenticationType.SIGN_UP);

		phoneAuthenticationService.validatePassword(request.getPassword(), request.getPasswordCheck());
		ResultResponse response;
		if (!memberService.isAvailableUsername(request.getUsername())) {
			response = ValidationResponse.fromValidatingUsername(request.getUsername(), false);
			return ResponseEntity.ok(response);
		}
		if (!memberService.isAvailablePhone(request.getPhone())) {
			response = ValidationResponse.fromValidatingPhone(request.getPhone(), false);
			return ResponseEntity.ok(response);
		}

		response = ValidationResponse.fromCheckingAuthenticationCode(result, request.getPhone());
		if (response.getCode().equals(AUTHENTICATION_CODE_MATCHED.getCode())) {
			memberService.createMember(request.getUsername(), request.getPhone(), EMPTY, request.getPassword(), EMPTY);
			response = ResultResponse.of(SIGN_UP_SUCCESS);
		}

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<ResultResponse> signin(SigninRequest request) {
		final SigninResponse result = memberService.signin(request.getUsername(), request.getPassword());
		final ResultResponse response = SigninResponseMapper.fromSigningIn(result);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<ResultResponse> findUsername(FindUsernameRequest request) {
		final PhoneAuthenticationResult result = phoneAuthenticationService.validateAuthentication(
			request.getPhone(), request.getAuthenticationKey(), PhoneAuthenticationType.FIND_ID);

		ResultResponse response = ValidationResponse.fromCheckingAuthenticationCode(result, request.getPhone());
		if (response.getCode().equals(AUTHENTICATION_CODE_MATCHED.getCode())) {
			final String username = memberService.getUsernameByPhone(request.getPhone());
			response = UsernameResponse.fromFindingUsername(username);
		}

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<ResultResponse> findPassword(FindPasswordRequest request) {
		final PhoneAuthenticationResult authResult = phoneAuthenticationService.validateAuthentication(
			request.getPhone(), request.getAuthenticationKey(), PhoneAuthenticationType.FIND_PW);

		ResultResponse response = ValidationResponse.fromCheckingAuthenticationCode(authResult,
			request.getPhone());
		if (response.getCode().equals(AUTHENTICATION_CODE_MATCHED.getCode())) {
			final boolean isExistentUsername = !memberService.isAvailableUsername(request.getUsername());
			response = ValidationResponse.fromCheckingUsername(isExistentUsername, request.getUsername());
			if (response.getCode().equals(USERNAME_EXISTENT.getCode())) {
				phoneAuthenticationService.validatePassword(request.getPassword(), request.getPasswordCheck());
				memberService.changePassword(request.getUsername(), request.getPassword());
				response = ResultResponse.of(PASSWORD_CHANGE_SUCCESS);
			}
		}

		return ResponseEntity.ok(response);
	}

}
