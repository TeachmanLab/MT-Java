package edu.virginia.psyc.pi.domain;

import edu.virginia.psyc.mindtrails.domain.Study;
import edu.virginia.psyc.mindtrails.domain.Task;
import edu.virginia.psyc.mindtrails.domain.tracking.TaskLog;
import lombok.Data;

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
@DiscriminatorValue("CBM Neutral")
public class CBMNeutralStudy extends CBMStudy implements Study {


    public CBMNeutralStudy() {}

    public CBMNeutralStudy(String currentName, int taskIndex, Date lastSessionDate, List<TaskLog> taskLogs) {
        super(currentName,taskIndex,lastSessionDate,taskLogs);
    }

    @Override
    protected List<Task> getTasks(NAME name, int taskIndex) {

        List<Task> tasks = new ArrayList<Task>();
        switch (name) {
            case PRE:
                tasks.add(new Task("credibility", "Consent to participate", Task.TYPE.questions, 2));
                tasks.add(new Task("demographics", "Demographics", Task.TYPE.questions, 2));
                tasks.add(new Task("MH", "Mental health history", Task.TYPE.questions, 2));
                tasks.add(new Task("QOL", "Satisfaction", Task.TYPE.questions, 0));
                //tasks.add(new Task("RecognitionRatings", "Completing short stories", Task.TYPE.playerScript, 0));
                //tasks.add(new Task("RR", "Completing short stories - Continued", Task.TYPE.questions, 0));
                tasks.add(new Task("BBSIQ", "Why things happen", Task.TYPE.questions, 0));
                tasks.add(new Task("DASS21_DS", "Mood assessment", Task.TYPE.questions, 0));
                tasks.add(new Task("DD", "Assessment", Task.TYPE.questions, 15));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                tasks.add(new Task("AnxietyTriggers", "Personal anxiety triggers", Task.TYPE.questions, 0));
                break;
            case SESSION1:
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions,0));
                tasks.add(new Task("Impact","Impact questions",Task.TYPE.questions,0));
                //tasks.add(new Task("FirstSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                //tasks.add(new Task("CC", "Follow up", Task.TYPE.questions,0));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                break;
            case SESSION2:
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions,0));
                tasks.add(new Task("Impact","Impact questions",Task.TYPE.questions,0));
                //tasks.add(new Task("SecondSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));

                break;
            case SESSION3:
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("Impact","Impact questions",Task.TYPE.questions,0));
                //tasks.add(new Task("ThirdSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                //tasks.add(new Task("CC", "Follow up", Task.TYPE.questions, 0));
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
                tasks.add(new Task("Impact","Impact questions",Task.TYPE.questions,0));
                //tasks.add(new Task("FourthSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                break;
            case SESSION5:
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("Impact","Impact questions",Task.TYPE.questions,0));
                //tasks.add(new Task("FifthSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));

                break;
            case SESSION6:
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("Impact","Impact questions",Task.TYPE.questions,0));
                //tasks.add(new Task("SixthSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                //tasks.add(new Task("CC", "Follow up", Task.TYPE.questions, 0));
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
                tasks.add(new Task("Impact","Impact questions",Task.TYPE.questions,0));
                //tasks.add(new Task("SeventhSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));

                break;
            case SESSION8:
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("ImageryPrime", "Use your imagination", Task.TYPE.questions, 0));
                tasks.add(new Task("Impact","Impact questions",Task.TYPE.questions,0));
                //tasks.add(new Task("EighthSessionSentences", "Training stories", Task.TYPE.playerScript, 20));
                tasks.add(new Task("SUDS", "How anxious you feel", Task.TYPE.questions, 0));
                //tasks.add(new Task("CC", "Follow up", Task.TYPE.questions, 0));
                tasks.add(new Task("RecognitionRatings", "Completing short stories", Task.TYPE.playerScript, 0));
                tasks.add(new Task("RR", "Completing short stories - Continued", Task.TYPE.questions, 0));
                tasks.add(new Task("BBSIQ", "Why things happen", Task.TYPE.questions, 0));
//                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                tasks.add(new Task("QOL", "How satisfied you feel", Task.TYPE.questions, 0));
                tasks.add(new Task("DASS21_DS", "Mood assessment", Task.TYPE.questions, 0));
                tasks.add(new Task("DD_FU", "Assessment", Task.TYPE.questions, 15));
                tasks.add(new Task("OA", "Anxiety review", Task.TYPE.questions, 1));
                tasks.add(new Task("DASS21_AS", "Recent anxiety symptoms", Task.TYPE.questions, 0));
                tasks.add(new Task("CIHS","Change in help seeking", Task.TYPE.questions, 1));

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
                tasks.add(new Task("CIHS","Change in help seeking", Task.TYPE.questions, 1));
                tasks.add(new Task("MUE", "Evaluating the program", Task.TYPE.questions, 2));

        }
        setTaskStates(name.toString(), tasks, taskIndex);
        return tasks;
    }


}
