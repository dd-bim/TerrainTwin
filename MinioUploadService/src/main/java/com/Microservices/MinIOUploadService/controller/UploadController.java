package com.Microservices.MinIOUploadService.controller;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import com.Microservices.MinIOUploadService.domain.model.UploadInfos;
import com.Microservices.MinIOUploadService.service.UploadService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@Controller
// @RequestMapping("/terraintwin")
@CrossOrigin(origins = "http://localhost:8084")
public class UploadController {

  // start page
  @GetMapping("/minioupload/home")
  public String index(Model model) throws Exception {
    model.addAttribute("erg", "");
    return "index";
  }

  // @Valid @RequestBody UploadInfos infos
  @PostMapping("/minioupload/send")
  public String uploadFileUI(@RequestParam("file") MultipartFile multipartFile, @RequestParam("bucket") String bucket,
      Model model) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
      InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException,
      IOException {
    UploadService minio = new UploadService();

    File file = minio.multipartToFile(multipartFile);

    UploadInfos infos = new UploadInfos(bucket, file);
    String results = minio.upload(infos);

    model.addAttribute("erg", results);

    return "index";
    // if (feedback.contains("400")) {
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(feedback);
    // } else {
    // return ResponseEntity.ok(feedback);
    // }
  }
}