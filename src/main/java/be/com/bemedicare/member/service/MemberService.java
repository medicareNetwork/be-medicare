package be.com.bemedicare.member.service;



import be.com.bemedicare.member.dto.MemberDTO;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MemberService {


    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void save(MemberDTO memberDTO) {

        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        //repository의 save 메서드 호출(조건 : entity 객체를 넘겨줘야 함)
        memberRepository.save(memberEntity);
    }

    public MemberEntity login(MemberEntity member) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(member.getMemberEmail());
        if (byMemberEmail.isPresent()) {
            MemberEntity memberEntity = byMemberEmail.get();

            if (memberEntity.getMemberPassword().equals(member.getMemberPassword())) {
                return memberEntity;
            } else {
                // 비밀번호 불일치
                return null;
            }
        } else {
            // 이메일이 존재하지 않음
            return null;
        }
    }

    public boolean checkEmail(String email) {
        return memberRepository.existsByMemberEmail(email);
    }


    public boolean checkName(String name) {
        return memberRepository.existsByMemberName(name);
    }

    public MemberEntity findMemberByAuthId(String authId) {
        return memberRepository.findByAuthId(authId);
    }

    public void saveKakaoMemberInfo(String authId, String nickName, String profileImageUrl, String email) {
        MemberEntity member = new MemberEntity();
        member.setAuthId(authId);
        member.setNickName(nickName);
        member.setProfileImageUrl(profileImageUrl);
        member.setEmail(email);


        memberRepository.save(member);
    }


    public String findEmailByNameAndNumber(String memberName, String memberNumber) {
        Optional<MemberEntity> member = memberRepository.findByMemberNameAndMemberNumber(memberName, memberNumber);
        return member.map(MemberEntity::getMemberEmail).orElse(null);
    }

    public String findPassword(String memberEmail, String memberQnA) {
        MemberEntity member = memberRepository.findByMemberEmailAndMemberQnA(memberEmail, memberQnA);
        if (member != null) {
            return member.getMemberPassword();
        } else {
            return null; // 또는 적절한 에러 메시지 반환
        }
    }

    public void update(MemberEntity memberEntity) {
        memberRepository.save(memberEntity);
    }

    public void changePassword(MemberEntity member) {
            memberRepository.save(member);
    }

    public void updateMemberAdditionalInfo(MemberEntity member) {
        memberRepository.save(member);
    }


}


