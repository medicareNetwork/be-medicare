package be.com.bemedicare.member.repository;



import be.com.bemedicare.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // 이메일과 이름으로 멤버가 존재하는지 확인하는 메서드
    boolean existsByMemberEmail(String memberEmail);
    boolean existsByMemberName(String memberName);

    Optional<MemberEntity> findByMemberEmail(String memberEmail);
    MemberEntity findByAuthId(String authId);
    MemberEntity findByMemberEmailAndMemberQnA(String memberEmail, String memberQnA);

    Optional<MemberEntity> findByMemberNameAndMemberNumber(String memberName, String memberNumber);
}
