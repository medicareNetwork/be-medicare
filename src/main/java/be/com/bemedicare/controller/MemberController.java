package be.com.bemedicare.controller;



import be.com.bemedicare.member.dto.ChangePasswordRequestDTO;
import be.com.bemedicare.member.dto.KakaoUserInfoResponseDto;
import be.com.bemedicare.member.dto.MemberDTO;
import be.com.bemedicare.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model) {
        try {
            MemberDTO loginResult = memberService.login(memberDTO);
            if (loginResult != null) {
                session.setAttribute("member", loginResult);
//                return "main"; // 성공 시 메인 페이지로 이동 <태립이가 만들어놓은곳으로 보낸것이 밑에>

                return "redirect:/board/list";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        // 로그인 실패 시 로그인 페이지로 돌아가면서 에러 메시지를 표시합니다.
        return "login";
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
    public String mypage(HttpSession httpSession, Model model) {
        MemberDTO memberDTO = (MemberDTO) httpSession.getAttribute("member");

        if (memberDTO == null) {
            return "login";
        }
        model.addAttribute("member", memberDTO);

        return "mypage";
    }

    @GetMapping("/update")
    public String updateForm(HttpSession httpSession, Model model) {
        MemberDTO loggedInMember = (MemberDTO) httpSession.getAttribute("member");

        model.addAttribute("memberDTO", loggedInMember);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model) {
        try {
            // 세션에서 로그인한 사용자 정보를 가져옴
            MemberDTO loggedInMember = (MemberDTO) session.getAttribute("member");

            // 기존의 회원 정보를 새롭게 입력한 데이터로 저장
            loggedInMember.setMemberName(memberDTO.getMemberName());
            loggedInMember.setMemberAge(memberDTO.getMemberAge());
            loggedInMember.setMemberHeight(memberDTO.getMemberHeight());
            loggedInMember.setMemberWeight(memberDTO.getMemberWeight());
            loggedInMember.setMemberAddress(memberDTO.getMemberAddress());
            loggedInMember.setMemberNumber(memberDTO.getMemberNumber());


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
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 HttpSession session,
                                 Model model) {

        MemberDTO resetPassword = (MemberDTO) session.getAttribute("member");


        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setCurrentPassword(currentPassword);
        request.setNewPassword(newPassword);
        request.setConfirmNewPassword(confirmNewPassword);

        boolean isPasswordChanged = memberService.changePassword(resetPassword.getMemberEmail(), request);

        if (isPasswordChanged) {
            model.addAttribute("member", resetPassword);
            return "mypage";
        } else {
            model.addAttribute("errorMessage", "현재 비밀번호가 잘못되었거나 새 비밀번호가 일치하지 않습니다.");
            return "changePassword";
        }
    }

}

