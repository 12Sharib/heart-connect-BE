package com.project.HeartConnect.utils.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.HeartConnect.dto.userdetails.MoreAboutUserDto;
import com.project.HeartConnect.dto.userdetails.UserBasicsDto;
import com.project.HeartConnect.entity.user.MoreAboutUser;
import com.project.HeartConnect.entity.user.UserBasics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityDtoMapper {
    private final ObjectMapper objectMapper;

    public UserBasics userBasicsDtoToEntity(final UserBasicsDto userBasicsDto){
        return objectMapper.convertValue(userBasicsDto, UserBasics.class);
    }
    public MoreAboutUser moreAboutUserDtoToEntity(final MoreAboutUserDto moreAboutUserDto){
        return objectMapper.convertValue(moreAboutUserDto, MoreAboutUser.class);
    }
}
