package stock.chart.login.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import stock.chart.domain.Member;
import stock.chart.domain.RefreshToken;
import stock.chart.domain.RefreshTokenStatus;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    @Modifying(clearAutomatically = true)
    @Query("update RefreshToken r set r.status = :refreshTokenStatus where r.member = :member")
    void updateAllByMember(@Param("member") Member member, @Param("refreshTokenStatus") RefreshTokenStatus refreshTokenStatus);

    @Query("select r from RefreshToken r join fetch r.member where r.refreshToken = :refreshToken")
    Optional<RefreshToken> findFetchByRefreshToken(@Param("refreshToken") String refreshToken);
}
