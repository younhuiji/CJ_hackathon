package org.traditionalAlcohol.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.traditionalAlcohol.Domain.SimAlcohol;

import java.util.List;

public interface SimAlcoholRepository extends JpaRepository<SimAlcohol, Integer> {
    // 이름에 대한 유사한 술 정보를 조회하는 메서드
    @Query(value = "SELECT * FROM sim_alcohol WHERE \"input_name\" LIKE %:name% ORDER BY \"sim\" DESC", nativeQuery = true)
    List<SimAlcohol> findSimilarAlcoholByName(@Param("name") String name);
}
