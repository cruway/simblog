package com.simblog.api.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class CommentCreate {

    @Length(min = 1, max = 8, message = "作成者は1~8文字まで入力してください。")
    @NotBlank(message = "作成者を入力してください。")
    private String author;

    @Length(min = 6, max = 30, message = "パスワードは6~30文字まで入力してください。")
    @NotBlank(message = "パスワードを入力してください。")
    private String password;

    @Length(min = 10, max = 1000, message = "内容は10~1000文字まで入力してください。")
    @NotBlank(message = "内容を入力してください。")
    private String content;

    @Builder
    public CommentCreate(String author, String password, String content) {
        this.author = author;
        this.password = password;
        this.content = content;
    }
}
