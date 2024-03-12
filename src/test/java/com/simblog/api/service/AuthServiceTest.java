package com.simblog.api.service;

import com.simblog.api.domain.User;
import com.simblog.api.exception.AlreadyExistsEmailException;
import com.simblog.api.repository.UserRepository;
import com.simblog.api.request.Signup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;
    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("会員登録成功")
    void test1() {
        // given
        Signup signup = Signup.builder()
                .email("sim89@gmail.com")
                .password("1234")
                .name("simblog")
                .build();

        // when
        authService.signup(signup);

        // then
        assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals("sim89@gmail.com", user.getEmail());
        assertNotNull(user.getPassword());
        assertEquals("simblog", user.getName());
    }

    @Test
    @DisplayName("会員登録の場合、重複したメール")
    void test2() {
        // given
        User user = User.builder()
                .email("sim89@gmail.com")
                .password("1234")
                .name("simblog2")
                .build();
        userRepository.save(user);

        Signup signup = Signup.builder()
                .email("sim89@gmail.com")
                .password("1234")
                .name("simblog")
                .build();

        // then
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));
    }
}