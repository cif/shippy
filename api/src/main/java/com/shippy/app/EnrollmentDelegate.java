package com.shippy.app;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Service
public class EnrollmentDelegate {

  @Value("${enrollment.endpoint}")
  private String ENROLLMENT_ENDPOINT;

  public Boolean isUserEnrolled(String sellerUsername) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    return false;
  }

  // private callService(String sellerUsername) {
  //   Closa
  // }
}
