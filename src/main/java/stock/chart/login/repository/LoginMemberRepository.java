package stock.chart.login.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stock.chart.domain.Member;
import stock.chart.domain.RefreshToken;

@Repository
public interface LoginMemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);


    @Query("select m from Member m where m.kakaoEmail = :email")
    Optional<Member> findByKakaoEmail(@Param("email") String email);
}
