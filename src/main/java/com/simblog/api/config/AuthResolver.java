package com.simblog.api.config;

import com.simblog.api.config.data.UserSession;
import com.simblog.api.exception.Unauthorized;
import com.simblog.api.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;
    private final AppConfig appConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class); // リクエストセッションDTOを支援するか
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jws = webRequest.getHeader("Authorization");
        if(jws == null || jws.equals("")) {
            throw new Unauthorized();
        }

       try {
           Jws<Claims> claims = Jwts.parser()
                   .setSigningKey(appConfig.getJwtKey())
                   .build()
                   .parseSignedClaims(jws);
            String userId = claims.getBody().getSubject();
            return new UserSession(Long.parseLong(userId));
        } catch (JwtException e) {
            //don't trust the JWT!
            throw new Unauthorized();
        }
    }
}