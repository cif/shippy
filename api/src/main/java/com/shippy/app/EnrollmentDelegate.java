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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EnrollmentDelegate {

  @Value("${enrollment.endpoint}")
  private String ENROLLMENT_ENDPOINT;

  Logger logger = LoggerFactory.getLogger(EnrollmentDelegate.class);

  public Boolean isUserEnrolled(String sellerUsername) {
    try {
      String response = callService(sellerUsername);
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
          HttpEntity entity = response.getEntity();
          if (entity != null) {
              return EntityUtils.toString(entity);
          }
          return null;

      } catch(Exception e) {
        logger.error(e.getMessage(), request);
        throw e;
      } finally {
          response.close();
      }
    } finally {
        httpClient.close();
    }
  }
}
