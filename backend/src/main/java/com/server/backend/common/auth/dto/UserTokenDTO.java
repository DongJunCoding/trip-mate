package com.server.backend.common.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserTokenDTO {

    private Long id;
    private String userId;
    private String refreshToken;

}
