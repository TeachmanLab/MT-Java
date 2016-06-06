package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.persistence.VisitDAO;
import edu.virginia.psyc.pi.persistence.VisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/public/track")
public class CounterController extends BaseController {

  private static final Logger LOG = LoggerFactory.getLogger(CounterController.class);
  @Autowired
  private VisitRepository visitRepository;

  @RequestMapping("{name}.gif")
  public String countEligible(@PathVariable String name) {
    LOG.info("Increment that counter right here!");
    VisitDAO counter = new VisitDAO(name);
    visitRepository.save(counter);
  return "redirect:/images/empty.gif";
  }


}
