package com.simblog.api.service;

import com.simblog.api.crypto.SCryptPasswordEncoder;
import com.simblog.api.domain.User;
import com.simblog.api.exception.AlreadyExistsEmailException;
import com.simblog.api.exception.InvalidSigninInformation;
import com.simblog.api.repository.UserRepository;
import com.simblog.api.request.Login;
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

    @Test
    @DisplayName("会員登録とログイン成功")
    void test3() {
        // given
        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder();
        String encrpytedPassword = encoder.encrypt("1234");

        User user = User.builder()
                .email("sim89@gmail.com")
                .password(encrpytedPassword)
                .name("simblog2")
                .build();
        userRepository.save(user);

        Login login = Login.builder()
                .email("sim89@gmail.com")
                .password("1234")
                .build();

        // when
        Long userId = authService.signin(login);

        // then
        assertNotNull(userId);
    }

    @Test
    @DisplayName("ログインの場合、パスワード失敗")
    void test4() {
        // given
        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder();
        String encrpytedPassword = encoder.encrypt("1234");

        Signup signup = Signup.builder()
                .email("sim89@gmail.com")
                .password(encrpytedPassword)
                .name("simblog")
                .build();
        authService.signup(signup);

        Login login = Login.builder()
                .email("sim89@gmail.com")
                .password("5678")
                .build();

        // expectd
        assertThrows(InvalidSigninInformation.class, () -> authService.signin(login));
    }
}