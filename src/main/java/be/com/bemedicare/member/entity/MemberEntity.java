package be.com.bemedicare.member.entity;


import be.com.bemedicare.member.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "member_table")
public class MemberEntity {

    //promary key 지점
    @Id
    // 정수형 열에 적용되며 새 행이 삽입될때 마다 자동으로 값을 증가시킨다
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long id;

    @Column(unique = true) //unique는 제약조건을 추가
    private String memberEmail;

    @Column
    private String memberPassword;

    @Column
    private String memberName;

    @Column
    private int memberAge;

    @Column
    private int memberWeight;

    @Column
    private int memberHeight;

    @Column(unique = true)
    private String memberNumber;

    @Column
    private String memberAddress;

    @Column
    private String memberQnA;

    @Column
    private String memberQ;

    // 여기서 부터 카카오 로그인  관련
    @Column
    private String authId;

    @Column
    private String nickName;

    @Column
    private String profileImageUrl;

    @Column
    private String email;


    // dto를 entity로 변환
    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberAge(memberDTO.getMemberAge());
        memberEntity.setMemberWeight(memberDTO.getMemberWeight());
        memberEntity.setMemberHeight(memberDTO.getMemberHeight());
        memberEntity.setMemberNumber(memberDTO.getMemberNumber());
        memberEntity.setMemberAddress(memberDTO.getMemberAddress());
        memberEntity.setMemberQnA(memberDTO.getMemberQnA());
        memberEntity.setMemberQ(memberDTO.getMemberQ());
        return memberEntity;
    }

}
