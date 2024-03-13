package com.simblog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simblog.api.config.SimblogMockUser;
import com.simblog.api.domain.Post;
import com.simblog.api.repository.PostRepository;
import com.simblog.api.repository.UserRepository;
import com.simblog.api.request.PostCreate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.simblog.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("コンテンツ1個照会テスト")
    void test1() throws Exception {
        // given
        Post post = Post.builder()
                .title("タイトル")
                .content("コンテンツ")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry", pathParameters(
                                parameterWithName("postId").description("コンテンツID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("コンテンツID"),
                                fieldWithPath("title").description("タイトル"),
                                fieldWithPath("content").description("コンテンツ")
                        )
                ));
    }

    @Test
    @SimblogMockUser
    @DisplayName("コンテンツ1個登録テスト")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("タイトル")
                .content("コンテンツ")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("タイトル")
                                        .attributes(key("constraint").value("良いタイトルを入力してください。")),
                                fieldWithPath("content").description("コンテンツ").optional()
                        )
                ));
    }
}
