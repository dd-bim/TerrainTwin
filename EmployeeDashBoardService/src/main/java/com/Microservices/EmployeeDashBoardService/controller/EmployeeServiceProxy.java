package com.Microservices.EmployeeDashBoardService.controller;

import java.util.Collection;

import com.Microservices.EmployeeDashBoardService.domain.model.EmployeeInfo;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;




@FeignClient(name = "EmployeeSearch")
//@RibbonClient(name = "EmployeeSearch")
@LoadBalancerClient(name = "EmployeeSearch")
public interface EmployeeServiceProxy {

 @RequestMapping("/employee/find/{id}")
 public EmployeeInfo findById(@PathVariable(value = "id") Long id);

 @RequestMapping("/employee/findall")
 public Collection < EmployeeInfo > findAll();

}
