package com.project.HeartConnect.utils.global;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class GlobalMethods {
  public String fetchCurrentUserMail(){
    log.info("Entry inside @class GlobalMethod @method fetchCurrentUserMail using token");

    return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
  }

}
