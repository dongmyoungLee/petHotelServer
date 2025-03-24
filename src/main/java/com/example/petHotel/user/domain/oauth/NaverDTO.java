package com.example.petHotel.user.domain.oauth;

import lombok.Getter;

@Getter
public class NaverDTO {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;

    @Getter
    public static class NaverUserInfoResponse {
        private String resultcode;
        private String message;
        private NaverUserInfo response;

        @Getter
        public static class NaverUserInfo {
            private String id;
            private String profile_image;
            private String email;
            private String mobile;
            private String mobile_e164;
            private String name;
        }
    }
}
