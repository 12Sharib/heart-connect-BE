package com.project.HeartConnect.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document("photos")
public class Photo {
    @Id
    private String id;
    @Field(name = "image")
    private Binary image;
}
