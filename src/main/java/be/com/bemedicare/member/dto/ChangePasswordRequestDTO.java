package be.com.bemedicare.member.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangePasswordRequestDTO {

    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;


}
