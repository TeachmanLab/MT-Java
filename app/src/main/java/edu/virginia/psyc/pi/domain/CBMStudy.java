package edu.virginia.psyc.pi.domain;

import edu.virginia.psyc.mindtrails.domain.BaseStudy;
import edu.virginia.psyc.mindtrails.domain.Session;
import edu.virginia.psyc.mindtrails.domain.Study;
import edu.virginia.psyc.mindtrails.domain.Task;
import edu.virginia.psyc.mindtrails.domain.tracking.TaskLog;
import lombok.Data;
import org.slf4j.LoggerFactory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 6/12/14
 * Time: 8:21 AM
 * The Participants progress through a series of sessions
 */
@Entity
@Table(name = "study")
@Data
@DiscriminatorValue("CBM")
public class CBMStudy extends BaseStudy implements Study {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CBMStudy.class);

    /**
     * This is an ordered enumeration, and is used that way.
     * Changing the order here will impact the order in which
     * sessions occur in the interface and in their progression.
     */
    public static enum NAME {
        ELIGIBLE, PRE, SESSION1, SESSION2, SESSION3, SESSION4, SESSION5, SESSION6, SESSION7, SESSION8, POST, COMPLETE
    }

    public CBMStudy() {}

    public CBMStudy(String currentSession, int taskIndex, Date lastSessionDate, List<TaskLog> taskLogs, boolean receiveGiftCards) {
        super(currentSession, taskIndex, lastSessionDate, taskLogs, receiveGiftCards);
    }

    /**
     * This specifies the gift amount, in dollars, that should be awarded when a user completes a session.
     */
    private int giftAmountCents(NAME name) {

        if (receiveGiftCards == false) return 0;
        if (name.equals(NAME.PRE) || name.equals(NAME.SESSION3) || name.equals(NAME.SESSION6) || name.equals(NAME.SESSION8))
            return 500; // $5
        if (name.equals(NAME.POST))
            return 1000; // $10
        return 0;
    }

    @Override
    public List<Session> getSessions() {
        List<Session> sessions = new ArrayList<Session>();
        boolean completed = true;
        boolean current = false;
        boolean gift;
        Session session;
        NAME curName = NAME.valueOf(this.currentSession);

        for (NAME name : NAME.values()) {
            if (name == curName) {
                completed = false;
                current = true;
            }
            gift = false;
            if (giftAmountCents(name) > 0) gift = true;
            if (!name.equals(NAME.ELIGIBLE)) {
                session = new Session(calcIndex(name), name.toString(), calculateDisplayName(name), completed, current, giftAmountCents(name), getTasks(name, currentTaskIndex));
                sessions.add(session);
            }
            current = false;  // only one can be current.
        }
        return sessions;
    }

    /**
     * In the future we should refactor this, so that we load the sessions from a
     * configuration file, or from the database.  But neither of these directions
     * seems clean to me, as they will naturally depend on certain code and paths
     * existing.  So here is how sessions and tasks are defined, until requirements,
     * or common sense dictate a more complex solution.
     * <p>
     * Note:  Arguments to Task are:  unique name, display name, type, and
     * approximate time it takes to complete in minutes.  If this value is 0, it
     * will not be displayed in the overview page.
     *
     * @param name The Name of a given session.
     * @return
     */
    protected List<Task> getTasks(NAME name, int taskIndex) {

        List<Task> tasks = new ArrayList<Task>();
        switch (name) {
            case PRE:
                tasks.add(new Task("Credibility", "Consent to participate", Task.TYPE.questions, 2));
                tasks.add(new Task("Demographic", "Demographics", Task.TYPE.questions, 2));
                tasks.add(new Task("MentalHealthHxTx", "Mental health history", Task.TYPE.questions, 2));
                tasks.add(new Task("QOL", "Satisfaction", Task.TYPE.questions, 0));
                tasks.add(new Task("RecognitionRatings", "Completing short stories", Task.TYPE.playerScript, 0));
                tasks.add(new Task("RR", "Completing short stories - Continued", Task.TYPE.questions, 0));
                tasks.add(new Task("BBSIQ", "Why things happen", Task.TYPE.questions, 0));
                tasks.add(new Task("DASS21_DS", "Mood assessment", Task.TYPE.questions, 0));
                tasks.add(new Task("DD", "Assessment", Task.TYPE.questions, 15));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                tasks.add(new Task("AnxietyTriggers", "Personal anxiety triggers", Task.TYPE.questions, 0));
                break;
            case SESSION1:
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("FirstSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("CC", "Follow up", Task.TYPE.questions, 0));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                break;
            case SESSION2:
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("SecondSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));

                break;
            case SESSION3:
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("ThirdSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("CC", "Follow up", Task.TYPE.questions, 0));
                tasks.add(new Task("RecognitionRatings", "Completing short stories", Task.TYPE.playerScript, 0));
                tasks.add(new Task("RR", "Completing short stories - Continued", Task.TYPE.questions, 0));
                tasks.add(new Task("BBSIQ", "Why things happen", Task.TYPE.questions, 0));
                tasks.add(new Task("QOL", "How satisfied you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("DASS21_DS", "Mood assessment", Task.TYPE.questions, 0));
                tasks.add(new Task("DD_FU", "Assessment", Task.TYPE.questions, 15));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
//                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                break;
            case SESSION4:
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("FourthSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                break;
            case SESSION5:
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("FifthSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));

                break;
            case SESSION6:
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("SixthSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("CC", "Follow up", Task.TYPE.questions, 0));
                tasks.add(new Task("RecognitionRatings", "Completing short stories", Task.TYPE.playerScript, 0));
                tasks.add(new Task("RR", "Completing short stories - Continued", Task.TYPE.questions, 0));
                tasks.add(new Task("BBSIQ", "Why things happen", Task.TYPE.questions, 0));
                tasks.add(new Task("QOL", "How satisfied you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("DASS21_DS", "Mood assessment", Task.TYPE.questions, 0));
                tasks.add(new Task("DD_FU", "Assessment", Task.TYPE.questions, 15));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
//                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                break;
            case SESSION7:
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("SeventhSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));

                break;
            case SESSION8:
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("ImpactAnxiousImagery", "Impact questions", Task.TYPE.questions, 0));
                tasks.add(new Task("EighthSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("CC", "Follow up", Task.TYPE.questions, 0));
                tasks.add(new Task("RecognitionRatings", "Completing short stories", Task.TYPE.playerScript, 0));
                tasks.add(new Task("RR", "Completing short stories - Continued", Task.TYPE.questions, 0));
                tasks.add(new Task("BBSIQ", "Why things happen", Task.TYPE.questions, 0));
//                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                tasks.add(new Task("QOL", "How satisfied you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("DASS21_DS", "Mood assessment", Task.TYPE.questions, 0));
                tasks.add(new Task("DD_FU", "Assessment", Task.TYPE.questions, 15));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                tasks.add(new Task("DASS21_AS", "Recent anxiety symptoms", Task.TYPE.questions, 0));
                tasks.add(new Task("CIHS", "Change in help seeking", Task.TYPE.questions, 1));

                break;
            case POST:
                tasks.add(new Task("RecognitionRatings", "Completing short stories", Task.TYPE.playerScript, 0));
                tasks.add(new Task("RR", "Completing short stories - Continued", Task.TYPE.questions, 0));
                tasks.add(new Task("BBSIQ", "Why things happen", Task.TYPE.questions, 0));

//                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                tasks.add(new Task("QOL", "How satisfied you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("DASS21_DS", "Mood assessment", Task.TYPE.questions, 0));
                tasks.add(new Task("DD_FU", "Assessment", Task.TYPE.questions, 15));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                tasks.add(new Task("DASS21_AS", "Recent anxiety symptoms", Task.TYPE.questions, 0));
                tasks.add(new Task("CIHS", "Change in help seeking", Task.TYPE.questions, 1));
                tasks.add(new Task("MultiUserExperience", "Evaluating the program", Task.TYPE.questions, 2));

        }
        setTaskStates(name.toString(), tasks, taskIndex);
        return tasks;
    }


    public static String calculateDisplayName(String name) {
        return calculateDisplayName(NAME.valueOf(name));
    }


    private static String calculateDisplayName(NAME name) {
        String displayName = "UNKNOWN";
        switch (name) {
            case ELIGIBLE:
                displayName = "Eligible";
                break;
            case PRE:
                displayName = "Initial Assessment";
                break;
            case SESSION1:
                displayName = "Day 1 Training";
                break;
            case SESSION2:
                displayName = "Day 2 Training";
                break;
            case SESSION3:
                displayName = "Day 3 Training";
                break;
            case SESSION4:
                displayName = "Day 4 Training";
                break;
            case SESSION5:
                displayName = "Day 5 Training";
                break;
            case SESSION6:
                displayName = "Day 6 Training";
                break;
            case SESSION7:
                displayName = "Day 7 Training";
                break;
            case SESSION8:
                displayName = "Day 8 Training";
                break;
            case POST:
                displayName = "2 Months Post Training";
                break;
            case COMPLETE:
                displayName = "Complete";
                break;
        }
        return displayName;
    }

    private static int calcIndex(NAME name) {
        int index = -1;
        switch (name) {
            case ELIGIBLE:
                index = -1;
                break;
            case PRE:
                index = -0;
                break;
            case SESSION1:
                index = 1;
                break;
            case SESSION2:
                index = 2;
                break;
            case SESSION3:
                index = 3;
                break;
            case SESSION4:
                index = 4;
                break;
            case SESSION5:
                index = 5;
                break;
            case SESSION6:
                index = 6;
                break;
            case SESSION7:
                index = 7;
                break;
            case SESSION8:
                index = 8;
                break;
            case POST:
                index = 9;
                break;
            case COMPLETE:
                index = 10;
                break;
        }
        return index;
    }


    @Override
    public boolean isReceiveGiftCards() {
        return receiveGiftCards;
    }

    @Override
    public void setReceiveGiftCards(boolean value) {
        receiveGiftCards = value;
    }


    @Override
    public STUDY_STATE getState() {

        if (getCurrentSession().getName().equals(NAME.COMPLETE.toString())) {
            return STUDY_STATE.ALL_DONE;
        }

        if (currentTaskIndex != 0) return STUDY_STATE.IN_PROGRESS;

        // Pre Assessment, Session 1, and 'Complete' can be accessed immediately.
        if (getCurrentSession().getName().equals(NAME.PRE.toString()) ||
                getCurrentSession().getName().equals(NAME.SESSION1.toString()))
            return STUDY_STATE.READY;

        // If you are on the POST assessment, you n     eed to wait 60 days after completing
        // session8
        if (getCurrentSession().getName().equals(NAME.POST.toString()) && daysSinceLastSession() < 60) {
            return STUDY_STATE.WAIT_FOR_FOLLOWUP;
        }

        // Otherwise, you must wait at least one dacomply before starting the next
        // session.
        if (daysSinceLastSession() == 0 && lastSessionDate != null) return STUDY_STATE.WAIT_A_DAY;

        // Otherwise it's time to start.
        return STUDY_STATE.READY;
    }


    @Override
    public String toString() {
        return "CBMStudy{" +
                "currentSession='" + currentSession + '\'' +
                ", taskIndex=" + currentTaskIndex +
                ", lastSessionDate=" + lastSessionDate +
                ", taskLogs=" + taskLogs +
                ", receiveGiftCards=" + receiveGiftCards +
                '}';
    }

}
