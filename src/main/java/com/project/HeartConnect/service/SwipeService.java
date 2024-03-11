package com.project.HeartConnect.service;

import com.project.HeartConnect.entity.user.User;
import com.project.HeartConnect.utils.response.GenericResponse;
import java.util.List;

public interface SwipeService {

  GenericResponse<String> swipeUser(String swipeId, Boolean swipeDirection);

  GenericResponse<List<User>> fetchUsersBasedOnLocation(Long latitude, Long longitude);
}
