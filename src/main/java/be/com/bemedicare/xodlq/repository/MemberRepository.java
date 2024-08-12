package be.com.bemedicare.xodlq.repository;

import be.com.bemedicare.xodlq.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    //이메일로 회원 정보 조회(select * from member where email=?)
    Optional<Member> findByEmail(String email);

}
