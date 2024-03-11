package com.project.HeartConnect.entity.swipe;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document("swiped")
public class Swipe {
  @Id
  private String id;

  @Field("user_mail")
  private String userMail;

  @Field("right_swiped_ids")
  private List<SwipeInfo> rightSwipedIds;

  @Field("left_swiped_ids")
  private List<SwipeInfo> leftSwipedIds;

}
