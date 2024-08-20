package be.com.bemedicare.controller;

import be.com.bemedicare.member.dto.MemberDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api/member/session")
    public ResponseEntity<MemberDTO> getSessionMember(HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("member");
        if( member != null) {
            return new ResponseEntity<>(member, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}
