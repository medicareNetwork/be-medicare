package be.com.bemedicare.member.controller;



import be.com.bemedicare.member.dto.MemberDTO;
import be.com.bemedicare.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
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
                session.setAttribute("loginEmail", loginResult.getMemberEmail());
                return "main"; // 성공 시 메인 페이지로 이동
            }
        } catch (IllegalArgumentException e) {
            // 서비스에서 발생한 예외를 잡아서 에러 메시지를 모델에 추가합니다.
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
    public String showFindPasswordPage() {
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
}

