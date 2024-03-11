package com.project.HeartConnect.utils.global;

import com.project.HeartConnect.exception.HeartConnectException;
import com.project.HeartConnect.repository.UserRepository;
import com.project.HeartConnect.utils.enums.ErrorEnums;
import com.project.HeartConnect.utils.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GlobalValidation {

  private final UserRepository userRepository;

  public void validateEmail(final String email) {
    if (Boolean.FALSE.equals(userRepository.existsByEmail(email))) {
      throw new HeartConnectException(new ErrorResponse(
          false, ErrorEnums.INVALID_EMAIL.getMessage()
      ));
    }
  }

  public void validateEmailFormat(final String email) {
    if (!email.contains("@") || !email.contains(".com")) {
      throw new HeartConnectException(
          new ErrorResponse(false, ErrorEnums.INVALID_EMAIL_FORMAT.getMessage()));
    }
  }

  public void validateGender(final String gender) {
    if (gender!=null && !(gender.trim().equalsIgnoreCase("male") ||
        gender.trim().equalsIgnoreCase("female") ||
        gender.trim().equalsIgnoreCase("trans"))) {
      throw new HeartConnectException(new ErrorResponse(
          false, ErrorEnums.INVALID_GENDER.getMessage()
      ));
    }
  }
}
