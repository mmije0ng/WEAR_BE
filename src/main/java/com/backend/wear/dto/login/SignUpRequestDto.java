package com.backend.wear.dto.login;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpRequestDto {
    @NotNull(message = "아이디 입력은 필수입니다.")
    private String id;

    @NotNull(message = "패스워드 입력은 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,16}$",
            message = "비밀번호는 8~16자 이내의 대문자와 숫자를 포함해야 합니다."
    )
    private String password;

    private String checkPassword;
    private String userName;
    private String nickName;
    private String universityName;
    private String universityEmail;
    private List<String> styleNameList=new ArrayList<>();
}