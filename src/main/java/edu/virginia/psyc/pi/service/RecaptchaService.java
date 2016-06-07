package edu.virginia.psyc.pi.service;

public interface RecaptchaService {
    boolean isResponseValid(String remoteIp, String response);
}
