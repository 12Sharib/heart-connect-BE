package com.project.HeartConnect.controller;


import com.project.HeartConnect.dto.UserInputDto;
import com.project.HeartConnect.entity.user.Preferences;
import com.project.HeartConnect.entity.user.User;
import com.project.HeartConnect.service.UserService;
import com.project.HeartConnect.utils.response.GenericResponse;
import com.project.HeartConnect.utils.wrapper.LoginWrapper;
import com.project.HeartConnect.utils.wrapper.TokenWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "APIs for User Management")
@RestController
@Log4j2
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(
      summary = "Login, fetch access token",
      description = "Api to fetch access token with login credentials",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Invalid Credentials"),
          @ApiResponse(responseCode = "401", description = "Unauthorized Access!")
      })
  @PostMapping("/token/login")
  public ResponseEntity<GenericResponse<TokenWrapper>> generateTokenLogin(
      @RequestBody final LoginWrapper loginWrapper) {
    return ResponseEntity.ok().body(userService.logInAndGenerateToken(loginWrapper));

  }

  @Operation(
      summary = "SignUp User",
      description = "Add valid credentials and create your account",
      responses = {
          @ApiResponse(responseCode = "200", description = "Success"),
          @ApiResponse(responseCode = "400", description = "Invalid details!"),
          @ApiResponse(responseCode = "500", description = "Internal Server Error")
      }
  )
  @PostMapping("/signup")
  public ResponseEntity<GenericResponse<String>> signupUser(
      @Valid @RequestBody UserInputDto userInputDto) {
    try {
      return ResponseEntity.ok().body(userService.createUser(userInputDto));
    } catch (Exception exception) {
      log.error("Error occurred while creating user", exception);
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(GenericResponse.error("Internal Server Error"));
    }
  }

  @Operation(
      summary = "Add user details",
      description = "Uploads multiple files and additional details associated with a user's email.",
      tags = {"User"},
      responses = {
          @ApiResponse(responseCode = "200", description = "User details uploaded successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid email, missing files, or invalid JSON data"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @PostMapping(
      value = "/add/details",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  @Parameter(name = "userDetailsDto",example = "{\n"
      + "  \"userBasics\": {\n"
      + "    \"work\": \" \",\n"
      + "    \"education\": \" \",\n"
      + "    \"gender\": \"  \",\n"
      + "    \"location\": \" \",\n"
      + "    \"hometown\": \" \"\n"
      + "  },\n"
      + "  \"moreAboutUserDto\": {\n"
      + "    \"height\": \" \",\n"
      + "    \"exercise\": \" \",\n"
      + "    \"educationLevel\": \" \",\n"
      + "    \"drinking\": \" \",\n"
      + "    \"smoking\": \" \",\n"
      + "    \"lookingFor\": \" \",\n"
      + "    \"kids\": \" \",\n"
      + "    \"starSign\": \" \",\n"
      + "    \"politics\": \" \",\n"
      + "    \"religion\": \" \"\n"
      + "  },\n"
      + "  \"languages\": [\n"
      + "    \" \",\n"
      + "    \" \"\n"
      + "  ]\n"
      + "}\n",
  description = "Update this json format to upload user basic data")
  public ResponseEntity<GenericResponse<String>> uploadUserDetails(
      @RequestParam("multipartFile") List<MultipartFile> multipartFiles,
      @RequestParam("userDetailsDto") String userDetailsDtoJson) {

    log.info("Entry inside @class SwipeController @method uploadUserDetails");

    return ResponseEntity.ok()
        .body(userService.addUserDetails(multipartFiles, userDetailsDtoJson));

  }

  @Operation(
      summary = "Retrieve User Information",
      description = "Fetches user details based on their email address.",
      tags = {"User"},
      responses = {
          @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid provided user email"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @GetMapping("/{userMail}")
  public ResponseEntity<GenericResponse<User>> getUserInfo(
      @Parameter(description = "Email address of the user") @PathVariable String userMail) {
    log.info("Retrieving user details for email: {}", userMail);

    return ResponseEntity.ok().body(userService.fetchUserDetailsByMail(userMail));
  }

  @Operation(
      summary = "Add user preferences",
      description = "Upload user preferences to match user.",
      responses = {
          @ApiResponse(responseCode = "200", description = "User preferences uploaded successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid provide user email"),
          @ApiResponse(responseCode = "500", description = "Internal server error")})
  @PostMapping("/preferences")
  public ResponseEntity<GenericResponse<String>> uploadUserPreferences(
      @RequestBody Preferences preferences) {
    log.info("Entry inside @class UserController @method uploadUserPreferences");

    return ResponseEntity.ok().body(userService.uploadPreferences(preferences));
  }

  @Operation(
      summary = "Block User",
      description = "Block a user, preventing them from being shown in future matches.",
      responses = {
          @ApiResponse(responseCode = "200", description = "User blocked successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid user email provided"),
          @ApiResponse(responseCode = "500", description = "Internal server error")})
  @Parameter(name = "blockUserMail", description = "Provide valid user mail that want to block")
  @PatchMapping("/block")
  public ResponseEntity<GenericResponse<String>> blockUser(
      @RequestParam("blockUserMail") String blockUserMail) {
    log.info("Entry inside @class UserController @method blockUser");

    return ResponseEntity.ok().body(userService.blockUser(blockUserMail));
  }

  @Operation(
      summary = "Unblock User",
      description = "Unblock a user, being shown in future matches.",
      responses = {
          @ApiResponse(responseCode = "200", description = "User unblocked successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid user email provided"),
          @ApiResponse(responseCode = "500", description = "Internal server error")})
  @Parameter(name = "unblockUserMail", description = "Provide valid user mail that want to unblock")
  @PatchMapping("/unblock")
  public ResponseEntity<GenericResponse<String>> unblockUser(
      @RequestParam("unblockUserMail") String unblockUserMail) {
    log.info("Entry inside @class UserController @method unblockUser");

    return ResponseEntity.ok().body(userService.unblockUser(unblockUserMail));
  }

  @Operation(
      summary = "Fetch all users",
      description = "Fetch list of all users to show in dashboard.",
      responses = {
          @ApiResponse(responseCode = "200", description = "Fetched users successfully"),
          @ApiResponse(responseCode = "500", description = "Internal server error"),
          @ApiResponse(responseCode = "401", description = "Unauthorized access")})
  @GetMapping("/users")
  public ResponseEntity<GenericResponse<List<User>>> getUsers() {
    log.info("Entry inside @class UserController @method getUsers");

    return ResponseEntity.ok().body(userService.fetchListOfUsers());
  }

  @Operation(
      summary = "Deactivate user account",
      description = "Deactivate user account that not be usable in future,",
      responses = {
          @ApiResponse(responseCode = "200", description = "Account deactivated successfully"),
          @ApiResponse(responseCode = "500", description = "Internal server error")})
  @PatchMapping("/deactivate")
  public ResponseEntity<GenericResponse<String>> deactivateAccount() {
    log.info("Entry inside @class UserController @method deactivateAccount");

    return ResponseEntity.ok().body(userService.deactivateAccount());
  }

  @Operation(summary = "Users Location-Based Matching",
      description = "If your user uses geolocation, API to match users based on their location.",
      responses = {
          @ApiResponse(responseCode = "200", description = "Fetched list of users"),
          @ApiResponse(responseCode = "400", description = "Invalid lat, long parameters"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @Parameters({
      @Parameter(name = "latitude", description = "Latitude of a user location."),
      @Parameter(name = "longitude", description = "Longitude of a user location.")
  })
  @GetMapping("/users/location")
  public ResponseEntity<GenericResponse<List<User>>> matchUserBasedOnLocation(
      @RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude) {
    log.info("Entry inside @class SwipeController @method matchUserBasedOnLocation");

    return ResponseEntity.ok().body(userService.fetchUsersBasedOnLocation(latitude, longitude));
  }

}
