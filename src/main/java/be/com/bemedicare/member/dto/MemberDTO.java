package be.com.bemedicare.member.dto;



import be.com.bemedicare.member.entity.MemberEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@ToString
@NotBlank
@Email
public class MemberDTO {
    private Long id;

    @NotBlank(message = "이메일 주소를 입력해주세요.")
   // @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String memberEmail;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String memberPassword;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String memberName;

    private int memberAge;
    private int memberWeight;
    private int memberHeight;
    private String memberNumber;
    private String memberAddress;
    private String memberQnA;



    //Entity를 DTO로 변환
    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        memberDTO.setMemberName(memberEntity.getMemberName());
        memberDTO.setMemberAge(memberEntity.getMemberAge());
        memberDTO.setMemberWeight(memberEntity.getMemberWeight());
        memberDTO.setMemberHeight(memberEntity.getMemberHeight());
        memberDTO.setMemberNumber(memberEntity.getMemberNumber());
        memberDTO.setMemberAddress(memberEntity.getMemberAddress());
        memberDTO.setMemberQnA(memberEntity.getMemberQnA());
        return memberDTO;
    }
}
