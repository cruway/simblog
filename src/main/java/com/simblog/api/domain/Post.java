package com.simblog.api.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

@Data
@Entity
@NoArgsConstructor(access = PUBLIC)
public class Post {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn
    private User user;

    @OneToMany(cascade = ALL, mappedBy = "post")
    private List<Comment> comments;

    @Builder
    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }

    public Long getUserId() {
        return this.user.getId();
    }

    public void addComment(Comment comment) {
        comment.setPost(this);
        this.comments.add(comment);
    }
}
