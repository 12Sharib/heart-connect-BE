package com.project.HeartConnect.service.impl;

import com.project.HeartConnect.entity.UserMatchChat;
import com.project.HeartConnect.entity.swipe.Swipe;
import com.project.HeartConnect.entity.swipe.SwipeInfo;
import com.project.HeartConnect.entity.user.User;
import com.project.HeartConnect.repository.UserMatchChatRepository;
import com.project.HeartConnect.repository.SwipeRepository;
import com.project.HeartConnect.service.SwipeService;
import com.project.HeartConnect.utils.constants.MessageConstants;
import com.project.HeartConnect.utils.response.GenericResponse;
import com.project.HeartConnect.utils.global.GlobalValidation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class SwipeServiceImpl implements SwipeService {

  private final GlobalValidation globalValidation;
  private final SwipeRepository swipeRepository;
  private final UserMatchChatRepository userMatchChatRepository;


  @Override
  public GenericResponse<String> swipeUser(final String swipeId, final Boolean swipeDirection) {
    // Log entry point
    log.info("Entry inside SwipeServiceImpl swipedUsers method");

    // Validate email
    globalValidation.validateEmail(swipeId);

    // Extract login user email from JWT token
    final String currentUserMail = SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal()
        .toString();

    // Fetch current user's swipes info to store the right/left swipe email in the corresponding list
    final Optional<Swipe> currentUser = swipeRepository.findByUserMail(currentUserMail);

    // If the current user is new
    if (currentUser.isEmpty()) {
      // Create his first entry in swipe table
      createFirstEntryInSwipeEntity(currentUserMail, swipeDirection, swipeId);

      return GenericResponse.success(MessageConstants.SWIPED_SUCCESS);
    }

    // Handle right/left swipe
    if (Boolean.TRUE.equals(swipeDirection)) {
      return GenericResponse.success(handleRightSwipe(currentUser.get(), swipeId));
    } else {
      handleLeftSwipe(currentUser.get(), swipeId);
    }
    // Return success response
    return GenericResponse.success(MessageConstants.SWIPED_SUCCESS);
  }

  @Override
  public GenericResponse<List<User>> fetchUsersBasedOnLocation(final Long latitude, final Long longitude) {
    log.info("Entry inside @class SwipeServiceImpl @method fetchUsersBasedOnLocation");
    return null;
  }

  private void createFirstEntryInSwipeEntity(final String currentUserMail,
      final Boolean swipeDirection,
      final String swipeId) {
    log.info("Entry inside @class FeatureServiceImpl @method createFirstEntryInSwipeEntity");
    final Swipe newUserSwipe = new Swipe();
    newUserSwipe.setUserMail(currentUserMail);

    final List<SwipeInfo> swipeInfos = new ArrayList<>();

    final SwipeInfo swipeInfo = new SwipeInfo();
    swipeInfo.setSwipeEmail(swipeId);
    swipeInfo.setDate(new Date());
    swipeInfos.add(swipeInfo);

    if (swipeDirection.equals(true)) {
      newUserSwipe.setRightSwipedIds(swipeInfos);
    } else {
      newUserSwipe.setLeftSwipedIds(swipeInfos);
    }
    swipeRepository.save(newUserSwipe);
    log.info("first info of swipe is saved success");
  }

  private String handleRightSwipe(final Swipe currentUser, final String swipeUserMail) {
    log.info("Entry inside @class FeatureServiceImpl @method handleRightSwipe");
    // Get the list of right-swiped ids, of the current user
    final List<SwipeInfo> rightSwipedListOfCurrentUser = currentUser.getRightSwipedIds();

    // Validate is user already swiped this user
    if (rightSwipedListOfCurrentUser.contains(swipeUserMail)){
      return MessageConstants.ALREADY_SWIPED_THIS_USER;
    }

    // Create a swipe info object for the current swipe
    final SwipeInfo swipeInfo = new SwipeInfo(swipeUserMail, new Date());

    // Add the swipe info to the list
    rightSwipedListOfCurrentUser.add(swipeInfo);

    // Set the updated list to the current user
    currentUser.setRightSwipedIds(rightSwipedListOfCurrentUser);

    // Save the updated info in the database
    swipeRepository.save(currentUser);

    // Fetch swipe info of the second user
    final Optional<Swipe> swipeSecond = swipeRepository.findByUserMail(swipeUserMail);

    if (swipeSecond.isPresent()) {
      // Get the list of right swiped ids, of the second user
      final List<SwipeInfo> rightSwipedListOfSecondUser = swipeSecond.get().getRightSwipedIds();

      // Check if there is a match
      if (rightSwipedListOfSecondUser.stream()
          .anyMatch(rightSwipe -> rightSwipe.getSwipeEmail().equals(currentUser.getUserMail()))) {

        // When it's a match, create chat space for these users
        createChatSpace(currentUser.getUserMail(), swipeUserMail);

        return MessageConstants.ITS_A_MATCH;
      } else {
        return MessageConstants.SWIPED_SUCCESS;
      }
    }
    return null;
  }

  private void createChatSpace(final String userMail, final String swipeUserMail) {
    final UserMatchChat userMatchChat = new UserMatchChat();
    userMatchChat.setUserMail(userMail);
    userMatchChat.setSwipedUserEmail(swipeUserMail);
    userMatchChat.setDate(new Date());

    // Save userMatchChat in database
    log.info("Saving userMatchChat info in DB @method createChatSpace");
    userMatchChatRepository.save(userMatchChat);
  }

  private void handleLeftSwipe(Swipe currentUser, String swipeId) {
    log.info("Entry inside @class FeatureServiceImpl @method handleLeftSwipe");
    // Get the list of left swiped ids, of the current user
    List<SwipeInfo> leftSwipedListOfCurrentUser = currentUser.getLeftSwipedIds();

    // Create a swipe info object for the current swipe
    final SwipeInfo swipeInfo = new SwipeInfo(swipeId, new Date());

    // Add the swipe info to the list
    leftSwipedListOfCurrentUser.add(swipeInfo);

    // Set the updated list to the current user
    currentUser.setLeftSwipedIds(leftSwipedListOfCurrentUser);

    // Save the updated info in the database
    swipeRepository.save(currentUser);
  }
}

