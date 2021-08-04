package com.Microservices.TestService.controller;

import com.Microservices.TestService.service.Service;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Test API", description = "Documentation")
public class TestRestController {

  @GetMapping("/test/")
  public String hello() {
      return "Hello World!";
  }

  @GetMapping("/test/{name}")
  public String helloService(@PathVariable String name) {
    Service service = new Service();
      return service.sayHello(name);
  }


}