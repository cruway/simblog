package com.simblog.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Login {

    @NotBlank(message = "メールを入力してください。")
    private String email;

    @NotBlank(message = "パスワードを入力してください。")
    private String password;
}
