package be.com.bemedicare.member.apiDTO;


import lombok.Data;

@Data
public class LoginRequest {

    private String memberEmail;
    private String memberPassword;
}
