package org.mindtrails.config;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tracking.ErrorLog;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ControllerAdvice
class GlobalDefaultExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";
    private static final Logger LOG = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);


    @Autowired
    EmailService emailService;

    @Autowired
    ParticipantService participantService;


    private void recordException(HttpServletRequest req, Exception e) {
        Participant p = currentParticipant(req);
        LOG.error("Raising an exception to an end user! " + e.getClass() + " --- " + e.getMessage());
        if(p != null) {
            ErrorLog log = new ErrorLog(p, req.getRequestURL().toString(), e);
            p.addErrorLog(log);
            participantService.save(p);
        }
    }

    private Participant currentParticipant(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        SecurityContextImpl sci = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (sci != null) {
            UserDetails cud = (UserDetails) sci.getAuthentication().getPrincipal();
            LOG.info("Error for user:" + cud);
            // do whatever you need here with the UserDetails
            if(cud instanceof Participant) return (Participant)cud;
        }
        return null;
    }


    @ExceptionHandler(value = Exception.class)
    public ModelAndView
    defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.

        recordException(req, e);
        e.printStackTrace();

        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("error", e.getClass());
        mav.addObject("message", e.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
}


