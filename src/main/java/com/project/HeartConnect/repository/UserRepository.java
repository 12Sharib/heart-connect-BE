package com.project.HeartConnect.repository;

import com.project.HeartConnect.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(Object email);

  @Query("{status:true}")
  List<User> findAllActiveUsers();

  List<User> findAllByUserBasicsLocationAndStatus(String cityName, Boolean status);
}
