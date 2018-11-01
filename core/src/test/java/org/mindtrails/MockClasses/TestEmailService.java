package org.mindtrails.MockClasses;

import org.mindtrails.domain.Email;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestEmailService extends EmailServiceImpl implements EmailService {
    @Override
    public List<Email> emailTypes() {
        List<Email> emails = super.emailTypes();
        emails.add(new Email("day2", "A day 2 email"));
        return emails;
    }
}