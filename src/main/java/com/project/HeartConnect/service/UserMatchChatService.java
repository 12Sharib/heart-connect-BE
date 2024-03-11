package com.project.HeartConnect.service;

import com.project.HeartConnect.utils.response.GenericResponse;

public interface UserMatchChatService {

  GenericResponse fetchUserMatchChats(String userMail);
}
