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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class KakaoLoginController {


    private final KakaoService kakaoService;


    @Autowired
    private MemberService memberService;
    @Autowired
    private KakaoUserInfo kakaoUserInfo;



    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, HttpSession session) {
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
            MemberEntity member = memberService.findMemberByAuthId(String.valueOf(userInfo.getId()));
            session.setAttribute("member", member);
            return "/kakaosave";
        } else {
            // 회원 정보가 있으면 로그인
            MemberEntity member = memberService.findMemberByAuthId(String.valueOf(userInfo.getId()));
            session.setAttribute("member", member);
            return "redirect:/board/list";
        }
    }

    @PostMapping("/saveAdditionalInfo")
    public String saveAdditionalInfo(
            @RequestParam(name="memberAge") int memberAge,
            @RequestParam(name="memberWeight") int memberWeight,
            @RequestParam(name="memberHeight") int memberHeight,
            @RequestParam(name="memberNumber") String memberNumber,
            @RequestParam(name="memberAddress") String memberAddress,
            HttpSession session) {

        MemberEntity member = (MemberEntity) session.getAttribute("member");

        if (member!=null){
            member.setMemberAge(memberAge);
            member.setMemberWeight(memberWeight);
            member.setMemberHeight(memberHeight);
            member.setMemberNumber(memberNumber);
            member.setMemberAddress(memberAddress);
            session.setAttribute("member",member);
        }

        // 회원의 추가 정보 업데이트
        memberService.updateMemberAdditionalInfo(member);

        // 업데이트 후 메인 페이지로 이동
        return "redirect:/board/list";
    }


}
