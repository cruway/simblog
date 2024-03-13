package com.simblog.api.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = SimblogMockSecurityContext.class)
public @interface SimblogMockUser {

    String name() default "sim";
    String email() default "sim89@gmail.com";
    String password() default "";

    //String role() default "ROLE_ADMIN";
}