package edu.virginia.psyc.mindtrails.controller;

import edu.virginia.psyc.mindtrails.domain.Visit;
import edu.virginia.psyc.mindtrails.persistence.VisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/public/track")
/**
 * Provides a way for external web sites to notify us of information we want to track.
 * For instance, if we have a partner that is doing some validation on a remote form,
 * and we want to keep track of the number of people that were not accepted into the
 * study, they could include a image that references http://[domain name]/public/track/notvalid.gif and
 * we can log the fact that they were not accepted.
 */
public class TrackingController {

  private static final Logger LOG = LoggerFactory.getLogger(TrackingController.class);
  @Autowired
  private VisitRepository visitRepository;

  @RequestMapping("{name}.gif")
  public String countEligible(@PathVariable String name) {
    Visit counter = new Visit(name);
    visitRepository.save(counter);
  return "redirect:/images/empty.gif";
  }


}
