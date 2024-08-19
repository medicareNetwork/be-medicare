package be.com.bemedicare.member.dto;



import be.com.bemedicare.member.entity.MemberEntity;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@ToString
public class MemberDTO {
    private Long id;


    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String memberEmail;

    private String memberPassword;

    private String memberName;

    private int memberAge;
    private int memberWeight;
    private int memberHeight;
    private String memberNumber;
    private String memberAddress;
    private String memberQ;
    private String memberQnA;


    private String authId;

    private String nickName;

    private String profileImageUrl;

    private String email;



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
        memberDTO.setMemberQ(memberEntity.getMemberQ());

        return memberDTO;
    }

    public static MemberEntity toEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberAge(memberDTO.getMemberAge());
        memberEntity.setMemberWeight(memberDTO.getMemberWeight());
        memberEntity.setMemberHeight(memberDTO.getMemberHeight());
        memberEntity.setMemberNumber(memberDTO.getMemberNumber());
        memberEntity.setMemberAddress(memberDTO.getMemberAddress());
        return memberEntity;
    }
}


