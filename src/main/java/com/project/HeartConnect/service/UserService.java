package com.project.HeartConnect.service;

import com.project.HeartConnect.dto.UserInputDto;
import com.project.HeartConnect.entity.user.Preferences;
import com.project.HeartConnect.entity.user.User;
import com.project.HeartConnect.utils.response.GenericResponse;
import com.project.HeartConnect.utils.wrapper.LoginWrapper;
import com.project.HeartConnect.utils.wrapper.TokenWrapper;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  GenericResponse<TokenWrapper> logInAndGenerateToken(LoginWrapper loginWrapper);

  GenericResponse<String> createUser(UserInputDto userInputDto);

  GenericResponse<String> addUserDetails(List<MultipartFile> multipartFile, String userDetailsDto);

  GenericResponse<User> fetchUserDetailsByMail(String userMail);

  GenericResponse<String> uploadPreferences(Preferences preferences);

  GenericResponse<String> blockUser(String blockUserMail);

  GenericResponse<String> unblockUser(String unblockUserMail);

  GenericResponse<List<User>> fetchListOfUsers();

  GenericResponse<String> deactivateAccount();

  GenericResponse<List<User>> fetchUsersBasedOnLocation(Double latitude, Double longitude);
}
