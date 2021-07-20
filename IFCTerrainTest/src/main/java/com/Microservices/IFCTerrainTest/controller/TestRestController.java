package com.Microservices.IFCTerrainTest.controller;

import java.io.IOException;

import com.Microservices.IFCTerrainTest.service.Service;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@Tag(name = "Test API", description = "Documentation")
public class TestRestController {

  // @GetMapping("/test/")
  // public String hello() {
  //     return "Hello World!";
  // }

  @PostMapping("/test/json")
  public String helloService(@RequestBody String name) throws IOException, InterruptedException {
    Service service = new Service();
      return service.sayHello(name);
  }


}