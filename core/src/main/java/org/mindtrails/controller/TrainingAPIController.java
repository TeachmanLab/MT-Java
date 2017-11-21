package org.mindtrails.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by any on 6/19/17.
 */

@Controller
@RequestMapping("/api/trainingAPI")
public class TrainingAPIController {
    @RequestMapping(method= RequestMethod.GET)
    public  @ResponseBody String sayHello(){
        return "hello";

    }
}
