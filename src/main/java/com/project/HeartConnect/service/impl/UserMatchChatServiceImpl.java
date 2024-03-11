package com.project.HeartConnect.service.impl;

import com.project.HeartConnect.entity.UserMatchChat;
import com.project.HeartConnect.repository.UserMatchChatRepository;
import com.project.HeartConnect.service.UserMatchChatService;
import com.project.HeartConnect.utils.response.GenericResponse;
import com.project.HeartConnect.utils.global.GlobalValidation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserMatchChatServiceImpl implements UserMatchChatService {
  private final GlobalValidation globalValidation;
  private final UserMatchChatRepository userMatchChatRepository;

  @Override
  public GenericResponse fetchUserMatchChats(final String userMail) {
    log.info("Entry inside @class UserMatchChatServiceImpl @method fetchUserMatchChats");

    // Validate user mail
    globalValidation.validateEmail(userMail);

    // Fetch all the match chats of this user
    final List<UserMatchChat> userMatchChatList = userMatchChatRepository.findAllByUserMail(userMail);

    // Response
    return GenericResponse.success(userMatchChatList);
  }
}
