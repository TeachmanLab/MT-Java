package org.mindtrails.MockClasses;

import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TestEmailService extends EmailServiceImpl implements EmailService {}
