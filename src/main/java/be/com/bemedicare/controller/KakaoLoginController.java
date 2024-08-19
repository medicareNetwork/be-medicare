package be.com.bemedicare.controller;


import be.com.bemedicare.member.dto.KakaoUserInfoResponseDto;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.service.KakaoService;
import be.com.bemedicare.member.service.KakaoUserInfo;
import be.com.bemedicare.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class KakaoLoginController {


    private final KakaoService kakaoService;

    @Autowired
    private MemberService memberService;
    @Autowired
    private KakaoUserInfo kakaoUserInfo;



    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code, HttpSession session) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        // 기존 회원 정보 확인
        MemberEntity existingMember = memberService.findMemberByAuthId(String.valueOf(userInfo.getId()));
        if (existingMember == null) {
            // 회원 정보가 없으면 회원가입 처리
            memberService.saveKakaoMemberInfo(
                    String.valueOf(userInfo.getId()),
                    userInfo.getKakaoAccount().getProfile().getNickName(),
                    userInfo.getKakaoAccount().getProfile().getProfileImageUrl(),
                    userInfo.getKakaoAccount().getEmail()
            );

            
            session.setAttribute("member", existingMember);
            return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
        } else {
            // 회원 정보가 있으면 로그인
            session.setAttribute("member", existingMember);
            return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
        }
    }
}

