package com.Microservices.Csv2RdfConverter.controller;

import com.Microservices.Csv2RdfConverter.service.Csv2RdfService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Microservices.Csv2RdfConverter.domain.model.ConvertInfos;

@RefreshScope
@RestController
public class Csv2rdfController {

    @Autowired
    Csv2RdfService converter;

    @RequestMapping("/csv2rdf")
    public String hello() {
        return "csv2rdf Converter";
    }

    @PostMapping("/csv2rdf/convert")
    public String convert(@RequestBody ConvertInfos infos) {
        return converter.convert(infos);
    }
}
