package com.shippy.app;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

import org.apache.http.HttpEntity;
// import org.apache.http.HttpHeaders;
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
    try {
      String response = callService(sellerUsername);
      System.out.println("GOT THE RESPONSE!");
      System.out.println(response);
      if (null == response) {
        return false;
      }
      // given the simplicty of the response, we can use string matching vs needing to parse.
      return response.indexOf("true") > 0;
    } catch (Exception e) {
      return false;
    }
  }

  private String callService(String sellerUsername) throws IOException {

    CloseableHttpClient httpClient = HttpClients.createDefault();

    try {
      HttpGet request = new HttpGet(ENROLLMENT_ENDPOINT + sellerUsername);
      CloseableHttpResponse response = httpClient.execute(request);
      try {

          // Get HttpResponse Status
          // System.out.println(response.getProtocolVersion());              // HTTP/1.1
          // System.out.println(response.getStatusLine().getStatusCode());   // 200
          // System.out.println(response.getStatusLine().getReasonPhrase()); // OK
          // System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

          HttpEntity entity = response.getEntity();
          if (entity != null) {
              return EntityUtils.toString(entity);
          }
          return null;

      } finally {
          response.close();
      }
    } finally {
        httpClient.close();
    }
  }
}
