package com.zoopi.infra.sms;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Primary
@Component
@RequiredArgsConstructor
public class NaverSensClient implements SmsClient {

	@Value("${sens.access-key}")
	private String ACCESS_KEY;

	@Value("${sens.secret-key}")
	private String SECRET_KEY;

	@Value("${sens.service-id}")
	private String SERVICE_ID;

	@Value("${sms.from}")
	private String FROM;

	private final RestTemplate restTemplate;

	public boolean sendSms(String phone, String content) {
		try {
			final String time = Long.toString(System.currentTimeMillis());

			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("x-ncp-apigw-timestamp", time);
			headers.set("x-ncp-iam-access-key", ACCESS_KEY);
			headers.set("x-ncp-apigw-signature-v2", getSignature(time));

			final List<SmsMessage> messages = new ArrayList<>();
			final SmsMessage smsMessage = new SmsMessage(phone, content);
			messages.add(smsMessage);
			final SmsRequest smsRequest = new SmsRequest(content, messages);

			final ObjectMapper objectMapper = new ObjectMapper();
			final String jsonBody = objectMapper.writeValueAsString(smsRequest);

			final HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
			final URI uri = new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + SERVICE_ID + "/messages");
			final ResponseEntity<SmsResponse> response = restTemplate.postForEntity(uri, request, SmsResponse.class);

			return response.getStatusCode().is2xxSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private String getSignature(String time) throws NoSuchAlgorithmException, InvalidKeyException {
		final String METHOD = "POST";
		final String SPACE = " ";
		final String URL = "/sms/v2/services/" + SERVICE_ID + "/messages";
		final String NEWLINE = "\n";
		final String HMAC_ALGORITHM = "HmacSHA256";
		final String message = METHOD + SPACE + URL + NEWLINE + time + NEWLINE + ACCESS_KEY;

		final SecretKeySpec signingKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
		final Mac mac = Mac.getInstance(HMAC_ALGORITHM);
		mac.init(signingKey);

		final byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(rawHmac);
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public class SmsRequest {

		private final String type = "SMS";
		private final String contentType = "COMM";
		private final String countryCode = "82";
		private final String from = FROM;
		private String content;
		private List<SmsMessage> messages = new ArrayList<>();

		public SmsRequest(String content, List<SmsMessage> messages) {
			this.content = content;
			this.messages = messages;
		}

	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SmsResponse {

		private String requestId;
		private String requestTime;
		private String statusCode;
		private String statusName;

	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	private static class SmsMessage {

		private String to;
		private String content;

	}

}
