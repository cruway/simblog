package com.simblog.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PostCreate {

    @NotBlank(message = "タイトルを入力してください。")
    private String title;

    @NotBlank(message = "コンテンツを入力してください。")
    private String content;

    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
