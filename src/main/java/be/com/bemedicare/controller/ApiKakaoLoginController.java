package be.com.bemedicare.controller;

import be.com.bemedicare.member.dto.KakaoUserInfoResponseDto;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.service.KakaoService;
import be.com.bemedicare.member.service.KakaoUserInfo;
import be.com.bemedicare.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ApiKakaoLoginController {

    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final KakaoUserInfo kakaoUserInfo;

    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code, HttpSession session, HttpServletResponse response) throws IOException {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        MemberEntity existingMember = memberService.findMemberByAuthId(String.valueOf(userInfo.getId()));

        boolean isNewUser;
        if (existingMember == null) {
            memberService.saveKakaoMemberInfo(
                    String.valueOf(userInfo.getId()),
                    userInfo.getKakaoAccount().getProfile().getNickName(),
                    userInfo.getKakaoAccount().getProfile().getProfileImageUrl(),
                    userInfo.getKakaoAccount().getEmail()
            );
            MemberEntity member = memberService.findMemberByAuthId(String.valueOf(userInfo.getId()));
            session.setAttribute("member", member);
            isNewUser = true;
        } else {
            session.setAttribute("member", existingMember);
            isNewUser = false;
        }

        // 리다이렉트 시 필요한 데이터를 쿼리 파라미터로 전달
        String redirectUrl = String.format("http://localhost:3000/callback?isNewUser=%s&userId=%s", isNewUser, userInfo.getId());
        response.sendRedirect(redirectUrl);
    }

    @PostMapping("/saveAdditionalInfo")
    public void saveAdditionalInfo(
            @RequestBody Map<String, Object> additionalInfo,
            HttpSession session) {

        MemberEntity member = (MemberEntity) session.getAttribute("member");

        if (member != null) {
            member.setMemberAge(Integer.parseInt((String) additionalInfo.get("age")));
            member.setMemberWeight(Integer.parseInt((String) additionalInfo.get("weight")));
            member.setMemberHeight(Integer.parseInt((String) additionalInfo.get("height")));
            member.setMemberNumber((String) additionalInfo.get("number"));
            member.setMemberAddress((String) additionalInfo.get("address"));
            session.setAttribute("member", member);
            System.out.println("member = " + member.getEmail());
            System.out.println("member.getMemberAge() = " + member.getMemberAge());
            System.out.println("member.getMemberPassword() = " + member.getMemberPassword());

            memberService.updateMemberAdditionalInfo(member);
        }
    }
}
