package stock.chart.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stock.chart.domain.Member;
import stock.chart.member.dto.MemberInfoDto;


public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select new stock.chart.member.dto.MemberInfoDto (m.id, m.email, m.nickname) from Member m where m.id = :id")
    Optional<MemberInfoDto> findMemberDtoById(@Param("id") Long id);

    @Query("select m from Member m where m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    @Query("select m from Member m where m.nickname = :nickname")
    Optional<Member> findByNickname(@Param("nickname") String nickname);
}
