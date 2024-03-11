package com.project.HeartConnect.dto.userdetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {
    private UserBasicsDto userBasics;
    private MoreAboutUserDto moreAboutUserDto;
    private List<String> languages;
}

