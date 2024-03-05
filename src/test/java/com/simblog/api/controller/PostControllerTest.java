package com.simblog.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private PostRepository postRepository;
//
//    @BeforeEach
//    void clean() {
//        postRepository.deleteAll();
//    }

    @Test
    @DisplayName("error responseテスト")
    void test() throws Exception {
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": null, \"content\":  \"contentdesu.\"}")
                ) // application/json
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("リクエスト失敗"))
                .andExpect(jsonPath("$.validation.title").value("タイトルを入力してください。"))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts リクエストの場合、DBに値を保存する")
    void test2() throws Exception {
        // when
//        mockMvc.perform(post("/posts")
//                        .contentType(APPLICATION_JSON)
//                        .content("{\"title\": \"タイトルです。\", \"content\":  \"コンテンツです。\"}")
//                ) // application/json
//                .andExpect(status().isOk())
//                .andDo(print());
//        // then
//        assertEquals(1L, postRepository.count());
//
//        Post post = postRepository.findAll().get(0);
//        assertEquals("タイトルです。", post.getTitle());
//        assertEquals("コンテンツです。", post.getContent());
    }
}