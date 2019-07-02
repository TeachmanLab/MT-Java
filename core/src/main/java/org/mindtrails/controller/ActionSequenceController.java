package org.mindtrails.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Anna on 7/2/19
 */


/* We need to do two main things:

    1. Record a participant's average latency between actions. This will be useful for finding predictors of attrition.
    2. In the Action database table, create 1 entry per 1 action duple (action1, action2) that includes:
        - The participant id
        - The questionnaire name
        - The latency time

 */  

@Controller
@RequestMapping("/action-sequence")
public class ActionController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(ActionRepository.class);

    @Autowired
    ActionRepository ActionRepository;


    // Set participant's average latency
    @ExportMode
    @RequestMapping(value="average", method = RequestMethod.POST,
            headers = "content-type=application/json")
    public @ResponseBody
    ResponseEntity<Void> saveAverageLatency(Principal principal,
                 @RequestBody ActionList actionList) {

        LOG.info("Recording participant's average latency")

        Participant p = getParticipant(principal);

        int num_actions = actionList.length
        int averageLatency = 0
        Date timestamp1, timestamp2;

        for(int i=0; i< num_actions-1; i++) {
            timestamp1 = actionList[i].getTimestamp()
            timestamp2 = actionList[i+1].getTimestamp()
            averageLatency = averageLatency + (timestamp1.getTime() - timestamp2.getTime())
        }
        
        averageLatency = averageLatency / float(num_actions)
        averageLatency = (participant.getAverageLatency() + averageLatency) / 2.0

        participant.setAverageLatency(averageLatency);
        participantService.save(participant);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Create entries in action table

}

