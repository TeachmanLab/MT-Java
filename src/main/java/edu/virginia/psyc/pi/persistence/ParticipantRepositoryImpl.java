package edu.virginia.psyc.pi.persistence;

import edu.virginia.psyc.pi.domain.*;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 6/24/14
 * Time: 1:30 PM
 * Allows us to inject a custom method into the automatically implemented
 * ParticipantRepository
 */
public class ParticipantRepositoryImpl implements ParticipantRepositoryCustom {

    @Override
    public Participant entityToDomain(ParticipantDAO dao) {
        Participant p = new Participant();
        List<Session> sessionList =  Session.createListView(dao.getCurrentSession(), dao.getTaskIndex());

        p.setId(dao.getId());
        p.setFullName(dao.getFullName());
        p.setEmail(dao.getEmail());
        p.setAdmin(dao.isAdmin());
        p.setSessions(sessionList);
        p.setTaskIndex(dao.getTaskIndex());
        p.setEmailOptout(dao.isEmailOptout());
        p.setActive(dao.isActive());
        p.setLastLoginDate(dao.getLastLoginDate());
        p.setLastSessionDate(dao.getLastSessionDate());
        p.setCbmCondition(dao.getCbmCondition());
        p.setPrime(dao.getPrime());


        // Password Tokens
        if(dao.getPasswordTokenDAO() != null) {
            p.setPasswordToken(new PasswordToken(dao.getPasswordTokenDAO().getToken(),
                                                 dao.getPasswordTokenDAO().getDateCreated()));
        }
        // Email Logs
        List<EmailLog> emailLogs = new ArrayList<EmailLog>();
        for(EmailLogDAO log : dao.getEmailLogDAOs()) {
            emailLogs.add(new EmailLog(log.getEmailType(), log.getDateSent()));
        }
        p.setEmailLogs(emailLogs);

        // Gift Logs
        List<GiftLog> giftLogs = new ArrayList<GiftLog>();
        for(GiftLogDAO log : dao.getGiftLogDAOs()) {
            giftLogs.add(new GiftLog(log.getOrderId(), log.getDateSent()));
        }
        p.setGiftLogs(giftLogs);

        return p;
    }

    @Override
    public void domainToEntity(Participant p, ParticipantDAO dao) {
        PasswordTokenDAO tokenDAO;

        dao.setId(p.getId());
        dao.setFullName(p.getFullName());
        dao.setEmail(p.getEmail());
        dao.setAdmin(p.isAdmin());
        dao.setTaskIndex(p.getTaskIndex());
        dao.setCurrentSession(p.getCurrentSession().getName());
        dao.setEmailOptout(p.isEmailOptout());
        dao.setActive(p.isActive());
        dao.setLastLoginDate(p.getLastLoginDate());
        dao.setLastSessionDate(p.getLastSessionDate());
        dao.setCbmCondition(p.getCbmCondition());
        dao.setPrime(p.getPrime());

        // Encrypt Password if it is set.
        if(p.getPassword() != null) {
            StandardPasswordEncoder encoder = new StandardPasswordEncoder();
            String hashedPassword = encoder.encode(p.getPassword());
            dao.setPassword(hashedPassword);
        }

        // Pass over any newly created password tokens
        if(p.getPasswordToken() != null) {
            if(dao.getPasswordTokenDAO() != null) tokenDAO = dao.getPasswordTokenDAO();
            else tokenDAO = new PasswordTokenDAO();

            tokenDAO.setDateCreated(p.getPasswordToken().getDateCreated());
            tokenDAO.setToken(p.getPasswordToken().getToken());
            dao.setPasswordTokenDAO(tokenDAO);
        } else {
            dao.setPasswordTokenDAO(null);
        }
    }


}
