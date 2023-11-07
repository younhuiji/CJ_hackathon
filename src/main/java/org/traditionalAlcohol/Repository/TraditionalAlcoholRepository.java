package org.traditionalAlcohol.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.traditionalAlcohol.Domain.TraditionalAlcohol;

import java.util.List;

public interface TraditionalAlcoholRepository extends JpaRepository<TraditionalAlcohol, Integer> {

    @Query(value = "SELECT * FROM (SELECT * FROM alcohol ORDER BY dbms_random.value) WHERE ROWNUM <= 3", nativeQuery = true)
    List<TraditionalAlcohol> findRandom3Alcohols();


    @Query(value = "SELECT * FROM (SELECT * FROM alcohol ORDER BY dbms_random.value) WHERE ROWNUM <= 2", nativeQuery = true)
    List<TraditionalAlcohol> findRandom2Alcohols();
}
