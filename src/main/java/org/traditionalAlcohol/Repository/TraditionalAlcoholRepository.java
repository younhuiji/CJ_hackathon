package org.traditionalAlcohol.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.traditionalAlcohol.Domain.TraditionalAlcohol;

import java.util.List;

public interface TraditionalAlcoholRepository extends JpaRepository<TraditionalAlcohol, Integer> {

    // 무작위로 3개의 전통주를 조회하는 메서드
    @Query(value = "SELECT * FROM (SELECT * FROM alcohol ORDER BY dbms_random.value) WHERE ROWNUM <= 3", nativeQuery = true)
    List<TraditionalAlcohol> findRandom3Alcohols();

    // 무작위로 2개의 전통주를 조회하는 메서드
    @Query(value = "SELECT * FROM (SELECT * FROM alcohol ORDER BY dbms_random.value) WHERE ROWNUM <= 2", nativeQuery = true)
    List<TraditionalAlcohol> findRandom2Alcohols();

    // 이름을 기준으로 리뷰 수 기준으로 가장 높은 술 1개 조회하는 메서드
    @Query(value = "SELECT *" +
            "FROM (" +
            "    SELECT *" +
            "    FROM alcohol" +
            "    WHERE \"en_foods\" LIKE %:name%" +
            "    ORDER BY \"reviewCount\" DESC" +
            ")" +
            "WHERE ROWNUM = 1", nativeQuery = true)
    List<TraditionalAlcohol> findByNameOrderByReviewCount(@Param("name") String name);

    // 이름을 기준으로 평점 기준으로 가장 높은 술 1개 조회하는 메서드
    @Query(value = "SELECT *" +
            "FROM (" +
            "    SELECT *" +
            "    FROM alcohol" +
            "    WHERE \"en_foods\" LIKE %:name%" +
            "    ORDER BY \"score\" DESC" +
            ")" +
            "WHERE ROWNUM = 1", nativeQuery = true)
    List<TraditionalAlcohol> findByNameOrderByScore(@Param("name") String name);
}
