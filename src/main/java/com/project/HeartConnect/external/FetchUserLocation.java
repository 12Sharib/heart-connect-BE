package com.project.HeartConnect.external;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Log4j2
public class FetchUserLocation {

  @Value("${location.url}")
  private String locationUrl;

  public String getLocationUsingLatAndLong(final Double latitude, final Double longitude) {
    log.info("Entry inside @class FetchUserLocation @method getLocationUsingLatAndLong");

    final String url = locationUrl.replace("{latitudeValue}", String.valueOf(latitude))
        .replace("{longitudeValue}", String.valueOf(longitude));

    final RestTemplate restTemplate = new RestTemplate();

    final ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      JsonNode responseBody = response.getBody();
      if (responseBody != null) {
        JsonNode addressNode = responseBody.get("address");

        JsonNode cityNode = addressNode.get("city");
        JsonNode districtNode = addressNode.get("state_district");

        final String name;

        if (!cityNode.isNull()) {
          name = cityNode.asText();
        } else if (!districtNode.isNull()) {
          name = districtNode.asText();
        } else {
          name = null;
        }
        return name;
      }
    }
    return null;
  }

}
