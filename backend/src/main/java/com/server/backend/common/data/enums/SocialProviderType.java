package com.server.backend.common.data.enums;

import lombok.Getter;

@Getter
public enum SocialProviderType {

    LOCAL("자체"),
    NAVER("네이버"),
    GOOGLE("구글"),
    KAKAO("카카오");

    private final String description;

    SocialProviderType(String description) {
        this.description = description;
    }
}
