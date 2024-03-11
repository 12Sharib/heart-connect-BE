package com.project.HeartConnect.repository;

import com.project.HeartConnect.entity.swipe.Swipe;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwipeRepository extends MongoRepository<Swipe, String> {

  Optional<Swipe> findByUserMail(String userMail);
}
