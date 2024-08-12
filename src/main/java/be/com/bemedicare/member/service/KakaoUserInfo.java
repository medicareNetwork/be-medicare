package be.com.bemedicare.member.service;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service

public class KakaoUserInfo {
    private String authId;
    private String nickName;
    private String profileImageUrl;
    private String email;
}
