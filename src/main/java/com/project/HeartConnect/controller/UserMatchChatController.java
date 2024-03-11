package com.project.HeartConnect.controller;

import com.project.HeartConnect.service.UserMatchChatService;
import com.project.HeartConnect.utils.response.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/chat") // Updated URL structure
@Log4j2
@RequiredArgsConstructor
@Tag(name = "UserMatchChat", description = "Contains User Match UserMatchChat APIs")
public class UserMatchChatController {

  private final UserMatchChatService chatService;

  @Operation(
      summary = "Get all user match chats",
      description = "Retrieves all chat conversations with matched users along with timestamps.",
      responses = {
          @ApiResponse(responseCode = "200", description = "Chats retrieved successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid user email provided"),
          @ApiResponse(responseCode = "401", description = "Unauthorized access")
      }
  )
  @GetMapping("/{userMail}")
  public ResponseEntity<GenericResponse> getAllUserMatchChats(@PathVariable String userMail) {
    log.info("Fetching user match chats for user with email: {}", userMail);

    // Delegate logic to service layer, handle potential exceptions gracefully
    return ResponseEntity.ok().body(chatService.fetchUserMatchChats(userMail));
  }
}
