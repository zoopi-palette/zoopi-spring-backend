package com.zoopi.infra.sms;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles(profiles = {"local", "test"})
@SpringBootTest(classes = {NaverSensClient.class, RestTemplate.class})
class NaverSensClientTest {

	@Autowired
	private SmsClient smsClient;

	@Value("${phone}")
	private String phone;

	@Test
	@Disabled
	void sendSms_phoneAndContent_true() throws Exception {
	    // given
		final String content = "테스트";

	    // when
		final boolean result = smsClient.sendSms(phone, content);

		// then
		assertThat(result).isTrue();
	}
}