package edu.virginia.psyc.r01.controller;

import edu.virginia.psyc.r01.domain.R01Study;
import edu.virginia.psyc.r01.persistence.ExpectancyBias;
import edu.virginia.psyc.r01.persistence.ExpectancyBiasRepository;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.mindtrails.controller.BaseController;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Session;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides a graph of the the Expectancy Bias results from a participant over time.
 */
@Controller
@RequestMapping("/graph")
public class GraphController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(GraphController.class);

    @Autowired private ExpectancyBiasRepository biasRepository;
    @Autowired private ParticipantService participantService;

    @RequestMapping
    public String graph(ModelMap model, Principal principal) {

        Participant p = participantService.findByEmail(principal.getName());
        List<ExpectancyBias> list    = biasRepository.findByParticipant(p);
        List<List<Object>> points    = new ArrayList();
        List<List<Object>> regressionPoints = new ArrayList();
        R01Study study = (R01Study)p.getStudy();

        Collections.sort(list);
        SimpleRegression regression;

        ExpectancyBias original = list.get(0);
        ExpectancyBias last     = list.get(list.size() - 1);

        regression = new SimpleRegression();
        double counter = 0;
        for(ExpectancyBias eb : list) {
                // don't include the post assessment when calculating the regression.
                 if (!eb.getSession().startsWith("POST")) {
                    regression.addData(counter, eb.score());
                    counter++;
                }
            }

        // Create plot points
        List<Object> point;
        for(ExpectancyBias eb : list) {
            point = new ArrayList<>();
            Session session = study.getSession(eb.getSession());
            if(session == null)
                point.add("");
            else
                point.add(session.getDisplayName());
            point.add(eb.score());
            points.add(point);

            if(eb.equals(original)) {
                ArrayList<Object> rPoint = new ArrayList<>(point);
                rPoint.set(1, regression.getIntercept());
                regressionPoints.add(rPoint);
            }
            if(eb.equals(last)) {
                ArrayList<Object> rPoint = new ArrayList<>(point);
                rPoint.set(1, regression.predict(list.size()));
                regressionPoints.add(rPoint);
            }
        }

        int improvement = new Double((regression.getIntercept() - regression.predict(list.size()))/regression.getIntercept() * 100).intValue();
        String status = "";
        if(Math.abs(improvement) < 15) status = "same";
        else if (improvement > 30) status = "lot";
        else if (improvement > 15) status = "little";
        else if (improvement < -15) status = "worse";

        model.addAttribute("points", points);
        model.addAttribute("regressionPoints", regressionPoints);
        model.addAttribute("improvement", improvement);
        model.addAttribute("status", status);
        model.addAttribute("participant", p);

        return "graph";

    }


}


