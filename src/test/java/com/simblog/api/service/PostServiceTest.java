package com.simblog.api.service;

import com.simblog.api.domain.Post;
import com.simblog.api.exception.PostNotFound;
import com.simblog.api.repository.PostRepository;
import com.simblog.api.request.PostCreate;
import com.simblog.api.request.PostEdit;
import com.simblog.api.request.PostSearch;
import com.simblog.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("ポスト作成テスト")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("タイトルです")
                .content("コンテンツです")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("タイトルです", post.getTitle());
        assertEquals("コンテンツです", post.getContent());
    }

    @Test
    @DisplayName("コンテンツ1個照会")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("コンテンツ複数照会")
    void test3() {
        // given
        postRepository.saveAll(
                List.of(
                        Post.builder()
                                .title("foo2")
                                .content("bar2")
                                .build(),
                        Post.builder()
                                .title("foo1")
                                .content("bar1")
                                .build()
                ));

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(5)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(2L, posts.size());
    }

    @Test
    @DisplayName("コンテンツ1ページ照会")
    void test4() {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("sim title " + i)
                        .content("sim content " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10L, posts.size());
        assertEquals("sim title 19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("コンテンツ修正")
    void test5() {
        // given
        Post post = Post.builder()
                .title("sim title")
                .content("sim content")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("sim edit title")
                .content("sim edit content")
                .build();
        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("コンテンツが存在しないです。 id=" + post.getId()));

        assertEquals("sim edit title", changePost.getTitle());
        assertEquals("sim edit content", changePost.getContent());
    }

    @Test
    @DisplayName("コンテンツ削除")
    void test6() {
        // given
        Post post = Post.builder()
                .title("sim title")
                .content("sim content")
                .build();

        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("コンテンツ1ページ照会")
    void test7() {
        // given
        Post post = Post.builder()
                .title("sim title")
                .content("sim content")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("コンテンツ削除 - 存在しないコンテンツ")
    void test8() {
        // given
        Post post = Post.builder()
                .title("sim title")
                .content("sim content")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("コンテンツ修正 - 存在しないコンテンツ")
    void test9() {
        // given
        Post post = Post.builder()
                .title("sim title")
                .content("sim content")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("sim edit title")
                .content("sim edit content")
                .build();

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);
        });
    }
}