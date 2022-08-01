package com.zoopi.domain.phoneauthentication.entity;

import static com.zoopi.domain.phoneauthentication.service.PhoneAuthenticationService.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.zoopi.domain.BaseEntity;
import com.zoopi.domain.phoneauthentication.dto.response.PhoneAuthenticationResult;

@Entity
@Getter
@Table(name = "phone_authentications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneAuthentication extends BaseEntity {

	@Id
	@Column(name = "phone_authentication_id")
	private String id;

	@Column(name = "code")
	private String code;

	@Column(name = "phone")
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private PhoneAuthenticationStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private PhoneAuthenticationType type;

	protected PhoneAuthentication(String id, String code, String phone, PhoneAuthenticationType type) {
		this.id = id;
		this.code = code;
		this.phone = phone;
		this.status = PhoneAuthenticationStatus.NOT_AUTHENTICATED;
		this.type = type;
	}

	/**
	 * 총 340,282,366,920,938,463,463,374,607,431,768,211,456개의 사용가능한 UUID <br>
	 * 매 초 10억개의 uuid를 100년에 걸쳐서 생성할 때 단 하나의 uuid가 중복될 확률은 50% <br>
	 * <a href="https://en.wikipedia.org/w/index.php?title=Universally_unique_identifier&oldid=755882275#Random_UUID_probability_of_duplicates">Reference</a>
	 */
	public static PhoneAuthentication of(String code, String phone, PhoneAuthenticationType type) {
		final String id = UUID.randomUUID().toString();
		return new PhoneAuthentication(id, code, phone, type);
	}

	public String getCodeExpiredDateToString() {
		return this.getCreatedAt()
			.plusMinutes(PHONE_AUTHENTICATION_CODE_VALID_MINUTES)
			.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 휴대폰 본인인증코드가 유효한지 확인한다.
	 * @param code 휴대폰 sms 문자로 받은 인증코드
	 * @return 인증코드 확인결과
	 */
	public PhoneAuthenticationResult checkCode(String code) {
		final boolean isExpired = this.isExpired(PHONE_AUTHENTICATION_CODE_VALID_MINUTES);
		final boolean isMatch = this.getCode().equals(code);

		if (isExpired) {
			this.expire();
			return PhoneAuthenticationResult.EXPIRED;
		} else if (isMatch) {
			this.authenticate();
			return PhoneAuthenticationResult.AUTHENTICATED;
		} else {
			return PhoneAuthenticationResult.MISMATCHED;
		}
	}

	/**
	 * 휴대폰 본인인증정보가 유효한지 검증한다.
	 * @return 인증결과
	 */
	public PhoneAuthenticationResult validate() {
		final boolean isExpired = this.isExpired(PHONE_AUTHENTICATION_KEY_VALID_MINUTES);
		final boolean isAuthenticated = this.getStatus().equals(PhoneAuthenticationStatus.AUTHENTICATED);

		if (isExpired) {
			this.expire();
			return PhoneAuthenticationResult.EXPIRED;
		} else if (isAuthenticated) {
			this.expire();
			return PhoneAuthenticationResult.AUTHENTICATED;
		} else {
			return PhoneAuthenticationResult.NOT_AUTHENTICATED;
		}
	}

	private boolean isExpired(long validMinutes) {
		return this.getCreatedAt()
			.plusMinutes(validMinutes)
			.isBefore(LocalDateTime.now());
	}

	private void authenticate() {
		this.status = PhoneAuthenticationStatus.AUTHENTICATED;
	}

	private void expire() {
		this.status = PhoneAuthenticationStatus.EXPIRED;
	}

}
