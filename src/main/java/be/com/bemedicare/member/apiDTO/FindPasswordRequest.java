package be.com.bemedicare.member.apiDTO;


import lombok.Data;

@Data
public class FindPasswordRequest {

    private String memberEmail;
    private String memberQnA;

}
