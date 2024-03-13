package com.simblog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simblog.api.domain.Comment;
import com.simblog.api.domain.Post;
import com.simblog.api.domain.User;
import com.simblog.api.repository.UserRepository;
import com.simblog.api.repository.comment.CommentRepository;
import com.simblog.api.repository.post.PostRepository;
import com.simblog.api.request.comment.CommentCreate;
import com.simblog.api.request.comment.CommentDelete;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @DisplayName("コメント作成")
    void test() throws Exception {
        // given
        User user = User.builder()
                .name("sim")
                .email("sim82@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("write title")
                .content("write content")
                .user(user)
                .build();
        postRepository.save(post);

        CommentCreate request = CommentCreate.builder()
                .author("sim33")
                .password("123456")
                .content("write content")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        assertEquals(1L, commentRepository.count());

        Comment savedComment = commentRepository.findAll().get(0);
        assertEquals("sim33", savedComment.getAuthor());
        assertNotEquals("123456", savedComment.getPassword());
        assertTrue(passwordEncoder.matches("123456", savedComment.getPassword()));
        assertEquals("write content", savedComment.getContent());
    }

    @Test
    @DisplayName("コメント削除")
    void test2() throws Exception {
        // given
        User user = User.builder()
                .name("sim")
                .email("sim82@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("write title")
                .content("write content")
                .user(user)
                .build();
        postRepository.save(post);

        String encryptedPassword = passwordEncoder.encode("123456");

        Comment comment = Comment.builder()
                .author("sim33")
                .password(encryptedPassword)
                .content("edit content message")
                .build();
        comment.setPost(post);
        commentRepository.save(comment);

        CommentDelete request = new CommentDelete("123456");
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/comments/{commentId}/delete", comment.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }
}