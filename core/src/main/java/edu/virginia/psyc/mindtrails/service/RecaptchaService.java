package edu.virginia.psyc.mindtrails.service;

public interface RecaptchaService {
    boolean isResponseValid(String remoteIp, String response);
}
