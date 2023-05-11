package com.example.noticeboard.dto.response;

import com.example.noticeboard.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "유저 응답 DTO")
public class ResUserDto {

    @Schema(description = "유저 id")
    private long id;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "권한")
    private String role;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시간")
    private LocalDateTime updatedAt;

    public static ResUserDto of(User user) {
        return ResUserDto.builder()
                  .id(user.getId())
                  .email(user.getEmail())
                  .role(user.getRole()
                            .name())
                  .name(user.getName())
                  .createdAt(user.getCreatedAt())
                  .updatedAt(user.getUpdatedAt())
                  .build();
    }
}
