package com.simblog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simblog.api.domain.Session;
import com.simblog.api.domain.User;
import com.simblog.api.repository.UserRepository;
import com.simblog.api.request.Login;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("ログイン後、セッション1個生成")
    void test() throws Exception {
        // given
        User user = userRepository.save(User.builder()
                .name("シム")
                .email("sim89@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("sim89@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        User loggedInUser = userRepository.findById(user.getId())
                        .orElseThrow(RuntimeException::new);

        assertEquals(1L, loggedInUser.getSessions().size());
    }

    @Test
    @DisplayName("ログイン後、accessToken 応答")
    void test2() throws Exception {
        // given
        User user = userRepository.save(User.builder()
                .name("シム")
                .email("sim89@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("sim89@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andDo(print());
    }

    @Test
    @DisplayName("ログイン後、権限が必要なページに接続する /foo")
    void test3() throws Exception {
        // given
        User user = userRepository.save(User.builder()
                .name("シム")
                .email("sim89@gmail.com")
                .password("1234")
                .build());
        Session session = user.addSession();
        userRepository.save(user);

        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("ログイン後、検証されてないセッション値で権限が必要なページに接続することができない")
    void test4() throws Exception {
        // given
        User user = userRepository.save(User.builder()
                .name("シム")
                .email("sim89@gmail.com")
                .password("1234")
                .build());
        Session session = user.addSession();
        userRepository.save(user);

        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken() + "-other" )
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized() )
                .andDo(print());
    }
}