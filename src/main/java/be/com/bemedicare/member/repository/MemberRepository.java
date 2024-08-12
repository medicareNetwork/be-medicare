package be.com.bemedicare.member.repository;



import be.com.bemedicare.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByMemberEmail(String memberEmail);
    boolean existsByMemberName(String memberName);

    Optional<MemberEntity> findByMemberEmail(String memberEmail);
    MemberEntity findByAuthId(String authId);
    MemberEntity findByMemberEmailAndMemberQnA(String memberEmail, String memberQnA);

    Optional<MemberEntity> findByMemberNameAndMemberNumber(String memberName, String memberNumber);
}
