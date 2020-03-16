package com.bungeeinc.bungeeapp.api.annotation;

import com.bungeeinc.bungeeapp.database.models.user.User;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActiveUser {
    UsernamePasswordAuthenticationToken TOKEN = ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication());
    User USER = (User)TOKEN.getPrincipal();
}
