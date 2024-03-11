package com.project.HeartConnect.repository;

import com.project.HeartConnect.entity.UserMatchChat;
import java.util.List;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMatchChatRepository extends MongoRepository<UserMatchChat, String> {

  @Aggregation(pipeline = {
      "{ $match: { $or: [{ user_mail: ?0 }, { swiped_user_mail: ?0 }] } }"
  })
  List<UserMatchChat> findAllByUserMail(String userMail);
}
