package com.simblog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simblog.api.config.SimblogMockUser;
import com.simblog.api.domain.Post;
import com.simblog.api.domain.User;
import com.simblog.api.repository.post.PostRepository;
import com.simblog.api.repository.UserRepository;
import com.simblog.api.request.post.PostCreate;
import com.simblog.api.request.post.PostEdit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

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
    @SimblogMockUser
    @DisplayName("コンテンツ作成")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("タイトルです。")
                .content("コンテンツです。")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .header("authorization", "simblog")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                ) // application/json
                .andExpect(status().isOk())
                .andDo(print());
        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("タイトルです。", post.getTitle());
        assertEquals("コンテンツです。", post.getContent());
    }

    @Test
    @DisplayName("コンテンツ1個照会")
    void test3() throws Exception {
        // given
        User user = User.builder()
                .name("sim2")
                .email("sim892@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .user(user)
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                ) // application/json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("foo"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("コンテンツ複数照会")
    void test4() throws Exception {
        // given
        User user = User.builder()
                .name("sim2")
                .email("sim892@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post1 = postRepository.save(Post.builder()
                .title("title_1")
                .content("content_1")
                .user(user)
                .build());

        Post post2 = postRepository.save(Post.builder()
                .title("title_2")
                .content("content_2")
                .user(user)
                .build());

        // expected
        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON)
                ) // application/json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id").value(post2.getId()))
                .andExpect(jsonPath("$[0].title").value("title_2"))
                .andExpect(jsonPath("$[0].content").value("content_2"))
                .andExpect(jsonPath("$[1].id").value(post1.getId()))
                .andExpect(jsonPath("$[1].title").value("title_1"))
                .andExpect(jsonPath("$[1].content").value("content_1"))
                .andDo(print());
    }

    @Test
    @DisplayName("コンテンツページ照会")
    void test5() throws Exception {
        // given
        User user = User.builder()
                .name("sim2")
                .email("sim892@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("sim title " + i)
                        .content("sim content " + i)
                        .user(user)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON)
                ) // application/json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("sim title 19"))
                .andExpect(jsonPath("$[0].content").value("sim content 19"))
                .andDo(print());
    }

    @Test
    @DisplayName("コンテンツページ0をリクエストすると最初のページに移動する")
    void test6() throws Exception {
        // given
        User user = User.builder()
                .name("sim2")
                .email("sim892@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("sim title " + i)
                        .content("sim content " + i)
                        .user(user)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON)
                ) // application/json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("sim title 19"))
                .andExpect(jsonPath("$[0].content").value("sim content 19"))
                .andDo(print());
    }

    @Test
    @SimblogMockUser
    @DisplayName("コンテンツ修正")
    void test7() throws Exception {
        // given
        var user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("sim title")
                .content("sim content")
                .user(user)
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("sim edit title")
                .content("sim edit content")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId()) // PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                ) // application/json
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @SimblogMockUser
    @DisplayName("コンテンツ削除")
    void test8() throws Exception {
        // given
        var user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("sim title")
                .content("sim content")
                .user(user)
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId()) // PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON)
                ) // application/json
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @SimblogMockUser
    @DisplayName("存在しないコンテンツ照会")
    void test9() throws Exception {
        // expected
        mockMvc.perform(delete("/posts/{postId}", 1L) // PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON)
                ) // application/json
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @SimblogMockUser
    @DisplayName("存在しないコンテンツを修正")
    void test10() throws Exception {
        // given
        PostEdit postEdit = PostEdit.builder()
                .title("sim edit title2")
                .content("sim edit content2")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L) // PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                ) // application/json
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}