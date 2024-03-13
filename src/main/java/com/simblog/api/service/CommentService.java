package com.simblog.api.service;

import com.simblog.api.domain.Comment;
import com.simblog.api.domain.Post;
import com.simblog.api.exception.CommentNotFound;
import com.simblog.api.exception.InvalidPassword;
import com.simblog.api.exception.PostNotFound;
import com.simblog.api.repository.comment.CommentRepository;
import com.simblog.api.repository.post.PostRepository;
import com.simblog.api.request.comment.CommentCreate;
import com.simblog.api.request.comment.CommentDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void write(Long postId, CommentCreate request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        Comment comment = Comment.builder()
                .author(request.getAuthor())
                .password(encryptedPassword)
                .content(request.getContent())
                .build();

        post.addComment(comment);
    }

    public void delete(Long commentId, CommentDelete request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        String encryptedPassword = comment.getPassword();
        if(!passwordEncoder.matches(request.getPassword(), encryptedPassword)) {
            throw new InvalidPassword();
        }

        commentRepository.delete(comment);
    }
}
