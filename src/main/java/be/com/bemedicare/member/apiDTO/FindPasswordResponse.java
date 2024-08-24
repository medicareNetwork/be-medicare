package be.com.bemedicare.member.apiDTO;


import lombok.Data;

@Data
public class FindPasswordResponse {

    private String memberPassword;

    public FindPasswordResponse(String memberPassword) {
        this.memberPassword = memberPassword;
    }
}
