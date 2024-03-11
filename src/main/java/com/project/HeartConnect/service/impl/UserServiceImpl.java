package com.project.HeartConnect.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.HeartConnect.dto.UserInputDto;
import com.project.HeartConnect.dto.userdetails.UserDetailsDto;
import com.project.HeartConnect.entity.Photo;
import com.project.HeartConnect.entity.user.Preferences;
import com.project.HeartConnect.entity.user.User;
import com.project.HeartConnect.exception.HeartConnectException;
import com.project.HeartConnect.external.FetchUserLocation;
import com.project.HeartConnect.repository.PhotoRepository;
import com.project.HeartConnect.repository.UserRepository;
import com.project.HeartConnect.security.JwtService;
import com.project.HeartConnect.service.UserService;
import com.project.HeartConnect.utils.constants.MessageConstants;
import com.project.HeartConnect.utils.enums.ErrorEnums;
import com.project.HeartConnect.utils.global.GlobalMethods;
import com.project.HeartConnect.utils.global.GlobalValidation;
import com.project.HeartConnect.utils.mapper.EntityDtoMapper;
import com.project.HeartConnect.utils.response.ErrorResponse;
import com.project.HeartConnect.utils.response.GenericResponse;
import com.project.HeartConnect.utils.wrapper.LoginWrapper;
import com.project.HeartConnect.utils.wrapper.TokenWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final GlobalValidation globalValidation;
  private final PhotoRepository photoRepository;
  private final EntityDtoMapper entityDtoMapper;
  private final GlobalMethods globalMethods;
  private final FetchUserLocation fetchUserLocation;
  BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  @Override
  public GenericResponse<TokenWrapper> logInAndGenerateToken(final LoginWrapper loginWrapper) {
    log.info("Entry inside @class UserServiceImpl @method logInAndGenerateToken");

    // Validate login credentials
    validateLoginCredentials(loginWrapper.getEmail(), loginWrapper.getPassword());

    final String token = JwtService.generateJwtToken(loginWrapper.getEmail());

    final TokenWrapper tokenWrapper = new TokenWrapper();
    tokenWrapper.setAccessToken(token);

    return GenericResponse.success(tokenWrapper);
  }

  private void validateLoginCredentials(final String email, final String password) {
    // Validate Email is valid or not
    globalValidation.validateEmail(email);

    final Optional<User> user = userRepository.findByEmail(email);

    if (user.isPresent() && !bCryptPasswordEncoder.matches(password, user.get().getPassword())) {
      throw new HeartConnectException(
          new ErrorResponse(false, ErrorEnums.INVALID_PASSWORD_TO_LOGIN.getMessage()));
    }
  }


  @Override
  public GenericResponse<String> createUser(final UserInputDto userInputDto) {
    log.info("Entry inside @class UserServiceImpl @method addUser");

    // Validate email format
    globalValidation.validateEmailFormat(userInputDto.getEmail());

    // Validate email already exists or not
    validateEmailExistence(userInputDto.getEmail());

    // Encode password
    final String password = bCryptPasswordEncoder.encode(userInputDto.getPassword());

    // Store user info in database
    userRepository.save(convertUserDtoToUser(userInputDto, password));
    log.info("User saved in database");
    return GenericResponse.success(MessageConstants.USER_CREATED_SUCCESS);
  }

  @Override
  public GenericResponse<String> addUserDetails(final List<MultipartFile> multipartFiles,
      final String userDetailsDtoJson) {
    log.info("Entry inside @class UserServiceImpl @method addUserDetails");

    // Convert the JSON string to UserDetailsDto
    ObjectMapper objectMapper = new ObjectMapper();
    UserDetailsDto userDetailsDto;
    try {
      userDetailsDto = objectMapper.readValue(userDetailsDtoJson, UserDetailsDto.class);
    } catch (final JsonProcessingException exception) {
      log.error("Error while converting string json data to class: ", exception);
      return GenericResponse.error(exception.getMessage());
    }
    // Validate gender
    globalValidation.validateGender(userDetailsDto.getUserBasics().getGender());

    // Fetch user email from token
    final String email = globalMethods.fetchCurrentUserMail();

    // Validate image(multipart) files
    validateImageFiles(multipartFiles);

    // Fetch user
    final Optional<User> user = userRepository.findByEmail(email);

    final Photo photo = new Photo();
    // List of photo ids to save in user entity
    final List<String> photoIds = new ArrayList<>();
    try {
      log.info("Started saving photo in database");

      for (MultipartFile file : multipartFiles) {
        // Set image binary data in photo entity
        photo.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));

        // Save photos in database
        final Photo savedPhoto = photoRepository.save(photo);
        log.info("Saved Photo in database");

        // Add ids of photos in a list of photos
        photoIds.add(savedPhoto.getId());
      }

      if (user.isPresent()) {
        // Set photo ids in user entity
        user.get().setPhotosIds(photoIds);
        user.get()
            .setUserBasics(entityDtoMapper.userBasicsDtoToEntity(userDetailsDto.getUserBasics()));
        user.get().setMoreAboutUser(
            entityDtoMapper.moreAboutUserDtoToEntity(userDetailsDto.getMoreAboutUserDto()));

        // Save user in a database with photo ids
        userRepository.save(user.get());
      }

    } catch (final IOException exception) {
      log.error("Exception while parse image file into binary ", exception);
      throw new HeartConnectException(
          new ErrorResponse(false, ErrorEnums.UNABLE_TO_PARSE_IMAGES.getMessage()));
    }
    // Response with success and a message of details saved successfully
    return GenericResponse.success(MessageConstants.DETAILS_SAVED);
  }

  @Override
  public GenericResponse<User> fetchUserDetailsByMail(final String userMail) {
    log.info("Entry inside @class UserServiceImpl @method fetchUserDetails");
    // Validate email format
    globalValidation.validateEmailFormat(userMail);

    // Validate email exists of not
    globalValidation.validateEmail(userMail);

    final Optional<User> user = userRepository.findByEmail(userMail);

    return user.map(GenericResponse::success).orElse(null);
  }

  @Override
  public GenericResponse<String> uploadPreferences(final Preferences preferences) {
    log.info("Entry inside @class UserServiceImpl @method uploadPreferences");

    // Validate gender
    globalValidation.validateGender(preferences.getGender());

    // Fetch current user mail using token
    final String currentUserMail = globalMethods.fetchCurrentUserMail();

    // Find the current user to set their preferences
    final Optional<User> user = userRepository.findByEmail(currentUserMail);

    if (user.isPresent()) {
      // Set user preferences
      user.get().setPreferences(preferences);
      // Saved user preferences in DB
      userRepository.save(user.get());

      return GenericResponse.success(MessageConstants.PREFERENCES_ADDED_SUCCESS);
    }
    return GenericResponse.error(null);
  }

  @Override
  public GenericResponse<String> blockUser(final String blockUserMail) {
    // Validate email format
    globalValidation.validateEmailFormat(blockUserMail);

    // Validate email exists or not
    globalValidation.validateEmail(blockUserMail);

    // Fetch current user from token
    final String currentUserMail = globalMethods.fetchCurrentUserMail();

    final Optional<User> user = userRepository.findByEmail(currentUserMail);

    if (user.isPresent()) {
      // Added this block user mail in the list of user-blocked
      user.get().setBlockedUsers(List.of(blockUserMail));

      // Update in DB
      userRepository.save(user.get());

      return GenericResponse.success(MessageConstants.BLOCKED_SUCCESS);
    }
    return GenericResponse.error(null);
  }

  @Override
  public GenericResponse<String> unblockUser(final String unblockUserMail) {
    // Validate unblock user mail
    globalValidation.validateEmailFormat(unblockUserMail);

    // Validate email is exists or not
    globalValidation.validateEmail(unblockUserMail);

    // Fetch current user from token
    final String currentUserMail = globalMethods.fetchCurrentUserMail();

    final Optional<User> user = userRepository.findByEmail(currentUserMail);

    if (user.isPresent()) {
      // Validate that unblock user mail is existing or not in current users blocked list
      final List<String> blockedUserList = user.get().getBlockedUsers();

      if (!blockedUserList.contains(unblockUserMail)) {
        throw new HeartConnectException(
            new ErrorResponse(false, ErrorEnums.INVALID_DOES_NOT_EXIST_TO_UNBLOCK.getMessage()));
      } else {
        // Remove from a blocked users list
        blockedUserList.remove(unblockUserMail);
        user.get().setBlockedUsers(blockedUserList);

        // Save updated blocked users list in DB
        userRepository.save(user.get());

        return GenericResponse.success(MessageConstants.UNBLOCKED_SUCCESS);
      }
    }
    return GenericResponse.error(null);
  }

  @Override
  public GenericResponse<List<User>> fetchListOfUsers() {
    log.info("Entry inside @class UserServiceImpl @method fetchListOfUsers");

    // Fetch users list
    final List<User> users = userRepository.findAllActiveUsers();

    return GenericResponse.success(users);
  }

  @Override
  public GenericResponse<String> deactivateAccount() {
    log.info("Entry inside @class UserServiceImpl @method deactivateAccount");

    // Fetch current user from token to deactivate
    final String currentUser = globalMethods.fetchCurrentUserMail();

    final Optional<User> user = userRepository.findByEmail(currentUser);

    if (user.isPresent()) {
      // Set status as false
      user.get().setStatus(false);

      // Save in DB
      userRepository.save(user.get());

      return GenericResponse.success(MessageConstants.USER_DEACTIVATED_SUCCESS);
    }
    return GenericResponse.error(null);
  }

  @Override
  public GenericResponse<List<User>> fetchUsersBasedOnLocation(final Double latitude,
      final Double longitude) {
    log.info("Entry inside @class UserServiceImpl @method fetchUsersBasedOnLocation");

    // Validate lat and long
    validateLatitudeAndLongitude(latitude, longitude);

    // Fetch user location using lat and long
    final String city = fetchUserLocation.getLocationUsingLatAndLong(latitude, longitude);

    if (city == null) {
      throw new HeartConnectException(
          new ErrorResponse(false, ErrorEnums.UNABLE_TO_FIND_LOCATION.getMessage()));
    }
    String cityName = null;
    if (city.contains(" ")) {
      String[] nameArr = city.split(" ");

      cityName = nameArr[0];
    }
    // Fetch all the users present in this city
    final List<User> users = userRepository.findAllByUserBasicsLocationAndStatus(cityName, true);

    return GenericResponse.success(users);
  }

  private void validateLatitudeAndLongitude(final Double latitude, final Double longitude) {
    if (!((latitude >= -90 && latitude <= 90) && (longitude >= -180 && longitude <= 180))) {
      throw new HeartConnectException(
          new ErrorResponse(false, ErrorEnums.INVALID_LAT_OR_LONG.getMessage()));
    }
  }

  private void validateImageFiles(final List<MultipartFile> multipartFiles) {
    if (multipartFiles.size() > 6) {
      throw new HeartConnectException(
          new ErrorResponse(false, ErrorEnums.EXCEEDS_MULTIPART_FILES_LIMIT.getMessage()));
    }
    for (MultipartFile multipartFile : multipartFiles) {
      String extension = getFileExtension(
          Objects.requireNonNull(multipartFile.getOriginalFilename()));
      if (!isSupportedExtension(extension)) {
        log.error("Invalid file extension: {}", extension);
        throw new HeartConnectException(
            new ErrorResponse(false, ErrorEnums.INVALID_FILE_EXTENSION.getMessage()));
      }
    }

  }

  private String getFileExtension(String filename) {
    int dotIndex = filename.lastIndexOf(".");
    if (dotIndex > 0) {
      return filename.substring(dotIndex + 1).toLowerCase();
    }
    return null; // No extension found
  }

  private boolean isSupportedExtension(final String extension) {
    return extension != null && (extension.equals("png") || extension.equals("jpg")
        || extension.equals("jpeg") || extension.equals("pdf"));
  }

  private void validateEmailExistence(final String email) {
    if (Boolean.TRUE.equals(userRepository.existsByEmail(email))) {
      throw new HeartConnectException(
          new ErrorResponse(false, ErrorEnums.EMAIL_ALREADY_EXISTS.getMessage()));
    }
  }

  private User convertUserDtoToUser(final UserInputDto userInputDto, final String password) {
    log.info("Entry inside @class UserServiceImpl @method convertUserDtoToUser");
    final User user = new User();
    user.setUsername(userInputDto.getUsername());
    user.setEmail(userInputDto.getEmail());
    user.setAge(userInputDto.getAge());
    user.setPassword(password);
    user.setStatus(true);

    return user;
  }


}
