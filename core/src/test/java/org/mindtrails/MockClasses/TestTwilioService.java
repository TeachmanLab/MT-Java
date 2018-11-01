package org.mindtrails.MockClasses;

import org.mindtrails.domain.Participant;
import org.mindtrails.service.TwilioService;
import org.mindtrails.service.TwilioServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TestTwilioService extends TwilioServiceImpl implements TwilioService {

    @Override
    public String getMessage(Participant p) {
        return "";
    }
}
