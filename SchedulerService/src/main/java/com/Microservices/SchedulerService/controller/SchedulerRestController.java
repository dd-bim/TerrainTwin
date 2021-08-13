package com.Microservices.SchedulerService.controller;

import java.util.concurrent.ScheduledFuture;

import com.Microservices.SchedulerService.domain.model.WorkingVar;
import com.Microservices.SchedulerService.service.ScheduledService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@RequestMapping("/scheduler")
@Tag(name = "Scheduler", description = "Start and stop the automatic file import request to the File Input Handler")
public class SchedulerRestController {

  public static final long FIXED_RATE = 10000;
  Logger log = LoggerFactory.getLogger(SchedulerRestController.class);
  
  WorkingVar worker  = new WorkingVar();

  @Autowired
  TaskScheduler taskScheduler;

  @Autowired
  ScheduledService service;

  ScheduledFuture<?> scheduledFuture;

  // start scheduled process
  @GetMapping("/start/{bucket}/{repo}")
  @Operation(summary = "Start the request for import to the File Input Handler with the specified parameters every 10 seconds")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  ResponseEntity<Void> start(@Parameter(description = "The name of the source MinIO bucket.") @PathVariable String bucket, @Parameter(description = "The name of the GraphDB repository.") @PathVariable String repo) {

    scheduledFuture = taskScheduler.scheduleAtFixedRate(schedule(bucket, repo), FIXED_RATE);
    worker.setWorkingVar(true);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  // stop scheduled process
  @GetMapping("/stop")
  @Operation(summary = "Stop executing the request")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  ResponseEntity<Void> stop() {
    scheduledFuture.cancel(false);
    worker.setWorkingVar(false);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  private Runnable schedule(String bucket, String repo) {
    return () -> log.info(service.fileInputHandler(bucket, repo));
}
}