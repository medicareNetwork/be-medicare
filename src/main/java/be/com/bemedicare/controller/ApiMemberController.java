package be.com.bemedicare.controller;


import be.com.bemedicare.member.apiDTO.*;
import be.com.bemedicare.member.dto.MemberDTO;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.rsocket.RSocketRequester;
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
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request, HttpSession session) {

        MemberEntity member = new MemberEntity();
        member.setMemberEmail(request.getMemberEmail());
        member.setMemberPassword(request.getMemberPassword());
        MemberEntity loginResult = memberService.login(member);
        if (loginResult != null) {
            session.setAttribute("member", loginResult);
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(request.getMemberEmail()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @PostMapping("/find-email")
    public ResponseEntity<LoginResponse> findEmail(@RequestBody @Valid EmailFind emailFind) {
        MemberEntity member = new MemberEntity();
        member.setMemberName(emailFind.getMemberName());
        member.setMemberNumber(emailFind.getMemberNumber());
        String memberEmail = memberService.findEmailByNameAndNumber(member.getMemberName(), member.getMemberNumber());
        if (memberEmail != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(memberEmail));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestBody CheckEmailRequest request) {
        boolean result = memberService.checkEmail(request.getMemberEmail());
        return ResponseEntity.ok(result);
    }


    @PostMapping("/find-password")
    public ResponseEntity<FindPasswordResponse> findPassword(@RequestBody @Valid FindPasswordRequest request) {
        MemberEntity member = new MemberEntity();
        member.setMemberEmail(request.getMemberEmail());
        member.setMemberQnA(request.getMemberQnA());
        String memberPassword = memberService.findPassword(request.getMemberEmail(), request.getMemberQnA());
        if (memberPassword != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new FindPasswordResponse(memberPassword));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/mypage")
    public ResponseEntity<MemberEntity> mypage(HttpSession session) {
        MemberEntity memberEntity = (MemberEntity) session.getAttribute("member");
        if (memberEntity != null) {
            return ResponseEntity.ok(memberEntity);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
