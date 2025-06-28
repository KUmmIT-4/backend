package com.kummit.api_server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 회원가입 요청을 받기 위한 DTO
 */
public class UserRegisterRequest {

    @NotBlank(message = "username은 반드시 입력해야 합니다.")
    private String username;

    @NotBlank(message = "password는 반드시 입력해야 합니다.")
    private String password;

    @Email(message = "email 형식이 올바르지 않습니다.")
    @NotBlank(message = "email은 반드시 입력해야 합니다.")
    private String email;

    @NotNull(message = "codingTier는 반드시 입력해야 합니다.")
    private String codingTier;

    @NotNull(message = "codingLevel은 반드시 입력해야 합니다.")
    @Min(value = 0, message = "codingLevel은 0 이상이어야 합니다.")
    @Max(value = 30, message = "codingLevel은 30 이하이어야 합니다.")
    private Integer codingLevel;

    @NotNull(message = "primaryLanguage는 반드시 입력해야 합니다.")
    private String primaryLanguage;

    public UserRegisterRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodingTier() {
        return codingTier;
    }

    public void setCodingTier(String codingTier) {
        this.codingTier = codingTier;
    }

    public Integer getCodingLevel() {
        return codingLevel;
    }

    public void setCodingLevel(Integer codingLevel) {
        this.codingLevel = codingLevel;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }
}
