package com.simblog.api.service;

import com.simblog.api.domain.Session;
import com.simblog.api.domain.User;
import com.simblog.api.exception.InvalidSigninInformation;
import com.simblog.api.repository.UserRepository;
import com.simblog.api.request.Login;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public Long signin(Login request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSigninInformation::new);
        Session session = user.addSession();

        return user.getId();
    }
}
