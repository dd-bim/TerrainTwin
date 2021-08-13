package com.Microservices.SchedulerService.controller;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import com.Microservices.SchedulerService.domain.model.WorkingVar;
import com.Microservices.SchedulerService.service.ScheduledService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RefreshScope
@Controller
@RequestMapping("/scheduler")
public class InterfaceController {

  public static final long FIXED_RATE = 10000;
  Logger log = LoggerFactory.getLogger(InterfaceController.class);
  
  WorkingVar worker = new WorkingVar();

  @Autowired
  TaskScheduler taskScheduler;

  @Autowired
  ScheduledService service;

  ScheduledFuture<?> scheduledFuture;

  // landing page scheduler
  @GetMapping("/home")
  public String index(Model model) throws Exception {

    String result = "AutoProcess is not enabled.";
    if (worker.getWorkingVar()) {
      result = "AutoProcess is enabled.";
    }

    model.addAttribute("erg", result);
    return "index";
  }

  // landing page actions on click buttons
  @PostMapping("/home")
  public String home(@RequestParam Map<String, String> data, Model model) throws Exception {
    String result = "";
    String bucket = data.get("bucket");
    String repo = data.get("repo");
    String action = data.get("action");

    // start scheduled process
    if (action.equals("start")) {
      scheduledFuture = taskScheduler.scheduleAtFixedRate(schedule(bucket, repo), FIXED_RATE);
      result = "AutoProcess started.";
      worker.setWorkingVar(true);
    } 
    // stop scheduled process
    else if (action.equals("stop")) {
      scheduledFuture.cancel(false);
      result = "AutoProcess stopped.";
      worker.setWorkingVar(false);
    }

    model.addAttribute("erg", result);
    return "index";
  }


  private Runnable schedule(String bucket, String repo) {
    return () -> log.info(service.fileInputHandler(bucket, repo));
}
}