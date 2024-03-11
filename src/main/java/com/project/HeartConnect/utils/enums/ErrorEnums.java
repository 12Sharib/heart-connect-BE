package com.project.HeartConnect.utils.enums;

import lombok.Getter;

@Getter
public enum ErrorEnums {
    INVALID_EMAIL_FORMAT("Invalid email format, provide valid email"),
    EMAIL_ALREADY_EXISTS("Invalid email, email already exist provide new email to sign up"),
    INVALID_EMAIL("Invalid email, provided email is not exists"),
    INVALID_PASSWORD_TO_LOGIN("Invalid password, provide valid password to login"),
    UNABLE_TO_PARSE_IMAGES("Error while converting image file to binary data or bytes"),
    EXCEEDS_MULTIPART_FILES_LIMIT("Cannot upload more than six images"),
    INVALID_FILE_EXTENSION("Invalid file extension, supported extensions are: png, jpg, jpeg, pdf"),
    INVALID_GENDER("Provide valid gender that should be Male, Female or Trans"),
    INVALID_DOES_NOT_EXIST_TO_UNBLOCK("Provide valid email, this email does not exists in current user's blocked list"),
    INVALID_LAT_OR_LONG("Invalid latitude or longitude, provide valid values latitude must be exist b/w -90 to 90 and longitude -180 to 180."),
    UNABLE_TO_FIND_LOCATION("Unable to find location, provide valid lat and long.");


    private final String message;
    ErrorEnums(String message){
        this.message = message;
    }
}
