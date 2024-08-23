package be.com.bemedicare.controller;



import be.com.bemedicare.member.dto.ChangePasswordRequestDTO;
import be.com.bemedicare.member.dto.KakaoUserInfoResponseDto;
import be.com.bemedicare.member.dto.MemberDTO;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.service.KakaoService;
import be.com.bemedicare.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//바꿔이새기야

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    //생성자  주입
    private final MemberService memberService;
    private final KakaoService kakaoService;

    @GetMapping("/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO, Model model) {
        try {
            memberService.save(memberDTO);
            return "login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "이메일이 중복됩니다");
            return "save";
        }
    }


    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }


    @PostMapping("/login")
    public String login(@RequestParam(name="memberEmail") String memberEmail,
                                    @RequestParam(name="memberPassword") String memberPassword,
                                   HttpSession session,
                                   Model model) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberEmail);
        memberEntity.setMemberPassword(memberPassword);
        try {
            MemberEntity loginResult = memberService.login(memberEntity);
            session.setAttribute("member", loginResult);
            return "redirect:/board/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login";

        }
        // 로그인 실패 시 로그인 페이지로 돌아가면서 에러 메시지를 표시합니다.
    }


    @GetMapping("/member-email/{email}/exists")
    public ResponseEntity<Boolean> checkEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.checkEmail(email));
    }

    @GetMapping("/member-name/{name}/exists")
    public ResponseEntity<Boolean> checkName(@PathVariable String name) {
        return ResponseEntity.ok(memberService.checkName(name));
    }

    //이메일 찾기
    @GetMapping("/find-email")
    public String showFindEmailForm() {
        return "find-email";
    }

    @PostMapping("/find-email")
    public String findEmail(@RequestParam String memberName, @RequestParam String memberNumber, Model model) {
        String memberEmail = memberService.findEmailByNameAndNumber(memberName, memberNumber);
        if (memberEmail != null) {
            model.addAttribute("memberEmail", memberEmail);
            return "email-found";
        } else {
            model.addAttribute("errorMessage", "일치하는 이메일이 없습니다");
            return "find-email";
        }
    }

    @GetMapping("/find-password")
    public String FindPasswordPage() {
        return "find-password"; // find-password.html 파일로 이동
    }


    @PostMapping("/find-password")
    public String findPassword(@RequestParam String memberEmail, @RequestParam String memberQnA, Model model) {
        String memberPassword = memberService.findPassword(memberEmail, memberQnA);
        if (memberPassword != null) {
            model.addAttribute("memberPassword", memberPassword);
            return "password-found"; // 비밀번호를 표시하는 HTML 페이지로 이동
        } else {
            model.addAttribute("errorMessage", "이메일 또는 답변이 일치하지 않습니다.");
            return "find-password"; // 비밀번호 찾기 페이지로 다시 이동
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @GetMapping("/mypage")
    public String mypage(HttpSession session) {
        MemberEntity memberEntity = (MemberEntity) session.getAttribute("member");

        if (memberEntity == null) {
            return "login";
        }

        return "mypage";
    }

    @GetMapping("/update")
    public String updateForm() {

        return "update";
    }

    @PostMapping("/update")
    public String update(MemberEntity memberEntity, HttpSession session, Model model) {
        try {
            // 세션에서 로그인한 사용자 정보를 가져옴
            MemberEntity loggedInMember = (MemberEntity) session.getAttribute("member");


            // 기존의 회원 정보를 새롭게 입력한 데이터로 저장
            loggedInMember.setMemberName(memberEntity.getMemberName());
            loggedInMember.setMemberAge(memberEntity.getMemberAge());
            loggedInMember.setMemberHeight(memberEntity.getMemberHeight());
            loggedInMember.setMemberWeight(memberEntity.getMemberWeight());
            loggedInMember.setMemberAddress(memberEntity.getMemberAddress());
            loggedInMember.setMemberNumber(memberEntity.getMemberNumber());

            //DB에 업데이트
            memberService.update(loggedInMember);

            // 업데이트한 정보를 다시 저장
            session.setAttribute("member", loggedInMember);

            return "redirect:/member/mypage";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "업데이트 중 오류가 발생했습니다.");
            return "update";
        }
    }

    @GetMapping("/changePassword")
    public String changePasswordSuccess() {
        return "changePassword";


    }


    @PostMapping("/changePassword")
    public String changePassword(ChangePasswordRequestDTO request,
                                 HttpSession session,
                                 Model model) {

        MemberEntity reset = (MemberEntity) session.getAttribute("member");

        if (reset.getMemberPassword().equals(request.getCurrentPassword()) &&
                request.getNewPassword().equals(request.getConfirmNewPassword())){
            reset.setMemberPassword(request.getNewPassword());
            session.setAttribute("member",reset);
            memberService.changePassword(reset);
            return "mypage";
        } else {
            model.addAttribute("errorMessage", "현재 비밀번호가 잘못되었거나 새 비밀번호가 일치하지 않습니다.");
            return "changePassword";
        }
    }

}

