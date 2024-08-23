package be.com.bemedicare.controller;


import be.com.bemedicare.member.dto.MemberDTO;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ApiMemberController {

    private final MemberService memberService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody MemberDTO memberDTO) {
        try {
            memberService.save(memberDTO);
            return ResponseEntity.ok(memberDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request, HttpSession session) {

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(request.memberEmail);
        memberEntity.setMemberPassword(request.memberPassword);
        try {
            MemberEntity loginResult = memberService.login(memberEntity);
            session.setAttribute("loginResult", loginResult);
            return ResponseEntity.status(HttpStatus.OK).body(loginResult);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Data
    static class LoginResponse {
        private String email;

        public LoginResponse(String email) {
            this.email = email;
        }
    }

    @Data
    static class LoginRequest {
        private String memberEmail;
        private String memberPassword;
    }


    @PostMapping("/find-email")
    public ResponseEntity<LoginResponse> findEmail(@RequestBody @Valid EmailFInd emailFInd) {
        MemberEntity member = new MemberEntity();
        member.setMemberName(emailFInd.memberName);
        member.setMemberNumber(emailFInd.memberNumber);
        String memberEmail = memberService.findEmailByNameAndNumber(member.getMemberName(), member.getMemberNumber());
        if (memberEmail != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(memberEmail));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Data
    static class EmailFInd {
        private String memberName;
        private String memberNumber;
    }


    @PostMapping("/find-password")
    public ResponseEntity<FindPasswordResponse> findPassword(@RequestBody @Valid FindPasswordRequest request) {
        MemberEntity member = new MemberEntity();
        member.setMemberEmail(request.memberEmail);
        member.setMemberQnA(request.memberQnA);
        String memberPassword = memberService.findPassword(request.memberEmail, request.memberQnA);
        if (memberPassword != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new FindPasswordResponse(memberPassword));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @Data
    static class FindPasswordRequest {
        private String memberEmail;
        private String memberQnA;
    }

    @Data
    static class FindPasswordResponse {
        private String memberPassword;

        public FindPasswordResponse(String memberPassword) {
            this.memberPassword = memberPassword;
        }
    }

}
