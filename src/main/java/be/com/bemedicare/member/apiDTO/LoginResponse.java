package be.com.bemedicare.member.apiDTO;


import lombok.Data;

@Data
public class LoginResponse {
    private String email;

    public LoginResponse(String email) {
        this.email = email;
    }
}
