package be.com.bemedicare.member.apiDTO;

import lombok.Data;

@Data
public class LoginSession {
    private String email;
    private String grade;
    private String address;

    public LoginSession(String email, String grade, String address) {
        this.email = email;
        this.grade = grade;
        this.address = address;
    }

}
