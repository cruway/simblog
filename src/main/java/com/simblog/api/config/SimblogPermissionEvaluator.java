package com.simblog.api.config;

import com.simblog.api.exception.PostNotFound;
import com.simblog.api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
public class SimblogPermissionEvaluator implements PermissionEvaluator {

    private final PostRepository postRepository;
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        var userPrincipal = (UserPrincipal) authentication.getPrincipal();

        var post = postRepository.findById((Long) targetId)
                .orElseThrow(PostNotFound::new);

        if(!post.getUserId().equals(userPrincipal.getUserId())) {
            log.error("[認可失敗] 該当するユーザが作成したコンテンツではありません。 targetId={}", targetId);
            return false;
        }

        return true;
    }
}
