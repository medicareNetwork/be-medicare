//package be.com.bemedicare.xodlq.service;
//
//import be.com.bemedicare.xodlq.entity.Member;
//import be.com.bemedicare.xodlq.repository.MemberRepository2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class MemberService2 {
//
//    @Autowired
//    private MemberRepository2 memberRepository;
//
//    public void createMember(Member member){
//        memberRepository.save(member);
//    }
//
//    public Optional<Member> loginMember(Member member){
//        Optional<Member> loginMember = memberRepository.findByEmail(member.getEmail());
//        if(loginMember.isPresent()){
//            if(loginMember.get().getPw().equals(member.getPw())){
//                return loginMember;
//            }else{
//                return null;
//            }
//        }else{
//            return null;
//        }
//    }
//
//    public List<Member> findAll(){
//        return null;
//    }
//
//}
