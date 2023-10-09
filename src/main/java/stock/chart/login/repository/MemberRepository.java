package stock.chart.login.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stock.chart.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
//    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByEmail(String email);
}
