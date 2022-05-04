package com.zoopi.infra.sms;

public interface SmsClient {

	boolean sendSms(String phone, String authenticationCode);
}
