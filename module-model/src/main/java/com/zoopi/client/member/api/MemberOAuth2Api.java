package com.zoopi.client.member.api;

import static com.zoopi.util.Constants.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.zoopi.ErrorResponse;
import com.zoopi.ResultResponse;
import com.zoopi.client.member.model.SigninAppRequest;
import com.zoopi.client.member.model.SigninResponse;

@Api(tags = "회원 간편 인증 API")
@RestController
@RequestMapping("/oauth2")
public interface MemberOAuth2Api {

	@ApiOperation(value = "간편 로그인 페이지 이동", notes = ""
		+ "간편 로그인 페이지로 redirect 후, 사용자가 로그인에 성공하면 \"http://localhost:3000/oauth2/callback/{provider}\"으로 redirect 됩니다." + BREAK_TAG
		+ "해당 url의 query parameter에서 code와 state를 얻을 수 있습니다.")
	@ApiImplicitParam(name = "provider", value = "OAuth2 provider", required = true, example = "naver")
	@GetMapping("/authorization/{provider}")
	void authorize(@PathVariable String provider);

	@ApiOperation(value = "간편 로그인 인증 코드 전달", notes = ""
		+ "먼저 간편 로그인 페이지 이동 API를 호출하여, redirect된 url의 query parameter에서 code와 state를 가져와서 API를 호출해야 합니다." + BREAK_TAG
		+ "API를 호출하면, 서버에서 인증 코드를 가지고 provider의 Access Token을 획득하고, 이를 이용해 provider의 유저 정보를 얻어냅니다." + BREAK_TAG
		+ "처음 로그인한 경우, 내부적으로 회원 가입 로직이 수행되며, 결과적으로 새로운 Access Token과 Refresh Token을 발급합니다.")
	@ApiImplicitParam(name = "provider", value = "OAuth2 provider", required = true, example = "naver")
	@GetMapping("/callback/{provider}")
	void callback(@PathVariable String provider, @RequestParam String code, @RequestParam String status);

	@ApiOperation(value = "간편 로그인(app)")
	@ApiResponses({
		@ApiResponse(code = 201, response = SigninResponse.class, message = ""
			+ "status: 200 | code: R-M006 | message: 로그인에 성공하였습니다."),
		@ApiResponse(code = 500, response = ErrorResponse.class, message = "Internal Server Error")
	})
	@PostMapping(value = "/signin/app", headers = "AccessToken")
	ResponseEntity<ResultResponse> signinApp(@Valid @RequestBody SigninAppRequest request);

}
