package com.capstone.backend.domain.product.repository;

import com.capstone.backend.domain.product.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, UUID> {

    @Query("SELECT MIN(ph.price) FROM PriceHistory ph WHERE ph.product.id = :productId")
    Integer findHistoricalLowest(@Param("productId") UUID productId);

    @Query(value = """
            SELECT DATE_TRUNC('day', recorded_at) AS date, MIN(price) AS price
            FROM price_histories
            WHERE product_id = :productId AND recorded_at >= :startDate
            GROUP BY DATE_TRUNC('day', recorded_at)
            ORDER BY date ASC
            """, nativeQuery = true)
    List<Object[]> findDailyHistory(@Param("productId") UUID productId,
                                    @Param("startDate") LocalDateTime startDate);

    @Query(value = """
            SELECT DATE_TRUNC('week', recorded_at) AS date, MIN(price) AS price
            FROM price_histories
            WHERE product_id = :productId AND recorded_at >= :startDate
            GROUP BY DATE_TRUNC('week', recorded_at)
            ORDER BY date ASC
            """, nativeQuery = true)
    List<Object[]> findWeeklyHistory(@Param("productId") UUID productId,
                                     @Param("startDate") LocalDateTime startDate);

    @Query(value = """
            SELECT DATE_TRUNC('month', recorded_at) AS date, MIN(price) AS price
            FROM price_histories
            WHERE product_id = :productId AND recorded_at >= :startDate
            GROUP BY DATE_TRUNC('month', recorded_at)
            ORDER BY date ASC
            """, nativeQuery = true)
    List<Object[]> findMonthlyHistory(@Param("productId") UUID productId,
                                      @Param("startDate") LocalDateTime startDate);
}
