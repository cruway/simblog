package com.simblog.api.request;

import com.simblog.api.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "タイトルを入力してください。")
    private String title;

    @NotBlank(message = "コンテンツを入力してください。")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (title.contains("バカ")) {
            throw new InvalidRequest();
        }
    }
}
