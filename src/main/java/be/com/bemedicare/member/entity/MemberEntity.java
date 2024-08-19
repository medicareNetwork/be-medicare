package be.com.bemedicare.member.entity;


import be.com.bemedicare.cart.entity.Cart;
import be.com.bemedicare.member.dto.MemberDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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


    @JsonIgnore//member를 json으로 변환 시 얘는 빼고 변환해라~
//    @OneToMany(mappedBy = "member") // 장바구니에서 member에있는 내용들을 꺼내쓸수있게
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Cart> cart = new ArrayList<>();


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

    public void update(MemberDTO memberDTO) {
        this.memberName = memberDTO.getMemberName();
        this.memberAge = memberDTO.getMemberAge();
        this.memberHeight = memberDTO.getMemberHeight();
        this.memberWeight = memberDTO.getMemberWeight();
        this.memberAddress = memberDTO.getMemberAddress();
        this.memberNumber = memberDTO.getMemberNumber();
    }

}
