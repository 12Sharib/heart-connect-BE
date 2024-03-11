package com.project.HeartConnect.entity.user;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Document("user")
public class User {
    @Id
    private String id;
    @Field("user_name")
    private String username;
    @Field("age")
    private Integer age;
    @Field("email")
    private String email;
    @Field("password")
    private String password;
    @Field("user_basics")
    private UserBasics userBasics;
    @Field("more_about_user")
    MoreAboutUser moreAboutUser;
    @Field("photos_ids")
    private List<String> photosIds;
    @Field("preferences")
    private Preferences preferences;
    @Field("blocked_users")
    private List<String> blockedUsers;
    @Field("status")
    private Boolean status;

}
