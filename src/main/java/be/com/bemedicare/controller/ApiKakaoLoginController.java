package be.com.bemedicare.controller;

import be.com.bemedicare.member.dto.KakaoUserInfoResponseDto;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.service.KakaoService;
import be.com.bemedicare.member.service.KakaoUserInfo;
import be.com.bemedicare.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiKakaoLoginController {

    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final KakaoUserInfo kakaoUserInfo;

    @GetMapping("/callback")
    public Map<String, Object> callback(@RequestParam("code") String code, HttpSession session) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        Map<String, Object> response = new HashMap<>();
        MemberEntity existingMember = memberService.findMemberByAuthId(String.valueOf(userInfo.getId()));

        if (existingMember == null) {
            memberService.saveKakaoMemberInfo(
                    String.valueOf(userInfo.getId()),
                    userInfo.getKakaoAccount().getProfile().getNickName(),
                    userInfo.getKakaoAccount().getProfile().getProfileImageUrl(),
                    userInfo.getKakaoAccount().getEmail()
            );
            MemberEntity member = memberService.findMemberByAuthId(String.valueOf(userInfo.getId()));
            session.setAttribute("member", member);
            response.put("isNewUser", true);
            response.put("member", member);
        } else {
            session.setAttribute("member", existingMember);
            response.put("isNewUser", false);
            response.put("member", existingMember);
        }

        return response;
    }

    @PostMapping("/saveAdditionalInfo")
    public void saveAdditionalInfo(
            @RequestBody Map<String, Object> additionalInfo,
            HttpSession session) {

        MemberEntity member = (MemberEntity) session.getAttribute("member");

        if (member != null) {
            member.setMemberAge((int) additionalInfo.get("age"));
            member.setMemberWeight((int) additionalInfo.get("weight"));
            member.setMemberHeight((int) additionalInfo.get("height"));
            member.setMemberNumber((String) additionalInfo.get("number"));
            member.setMemberAddress((String) additionalInfo.get("address"));
            session.setAttribute("member", member);

            memberService.updateMemberAdditionalInfo(member);
        }
    }
}
