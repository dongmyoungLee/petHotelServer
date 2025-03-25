package com.example.petHotel.user.domain.oauth;

import lombok.Getter;

@Getter
public class GoogleDTO {
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;

    @Getter
    public static class GoogleUserInfo {
        private String sub;
        private String name;
        private String given_name;
        private String family_name;
        private String picture;
        private String email;
        private Boolean email_verified;
    }
}
