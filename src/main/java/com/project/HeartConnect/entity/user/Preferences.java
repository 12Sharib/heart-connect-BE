package com.project.HeartConnect.entity.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Preferences {

  private String gender;
  private String location;
  @Min(value = 1, message = "Provide valid range value shouldn't be less than 1")
  private Integer range;

  @Min(value = 18, message = "Provide valid age value shouldn't be less than 18")
  private Integer minAge;
  @Max(value = 60, message = "Provide valid age value shouldn't be greater than 60")
  private Integer maxAge;

  private Integer minHeight;
  private Integer maxHeight;
}
