package com.project.HeartConnect.entity;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document("user_match_chat")
public class UserMatchChat {

  @Id
  private String id;
  @Field("user_mail")
  private String userMail;
  @Field("swiped_user_email")
  private String swipedUserEmail;
  @Field("date_time")
  private Date date;

}
