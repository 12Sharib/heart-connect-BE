package com.project.HeartConnect.controller;

import com.project.HeartConnect.entity.user.User;
import com.project.HeartConnect.service.SwipeService;
import com.project.HeartConnect.utils.response.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/swipe")
@Log4j2
@RequiredArgsConstructor
@Tag(name = "Swipe", description = "APIs for Swiping User Profiles")
public class SwipeController {

  private final SwipeService swipeService;

  @Operation(summary = "Swipe User Profile to match", description = "Performs a left or right swipe on a user profile.", responses = {
      @ApiResponse(responseCode = "200", description = "Swipe registered successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid swipe ID or direction"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access"),
      @ApiResponse(responseCode = "409", description = "User already swiped on this profile")})
  @Parameters({
      @Parameter(name = "swipeProfileId", description = "Unique identifier of the user profile being swiped on.", required = true),
      @Parameter(name = "swipeDirection", description = "Direction of the swipe (true for right, false for left).", required = true)})
  @PostMapping("/match")
  public ResponseEntity<GenericResponse<String>> swipeUser(
      @RequestParam("swipeProfileId") String swipeId,
      @RequestParam("swipeDirection") Boolean swipeDirection) {
    log.info("Processing swipe on user profile {} with direction {}", swipeId, swipeDirection);

    // Delegate logic to service, handle potential exceptions gracefully
    return ResponseEntity.ok().body(swipeService.swipeUser(swipeId, swipeDirection));
  }


}
