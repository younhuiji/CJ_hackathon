package org.traditionalAlcohol.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.traditionalAlcohol.Domain.TraditionalAlcohol;
import org.traditionalAlcohol.Repository.TraditionalAlcoholRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraditionalAlcoholService {
    private final TraditionalAlcoholRepository traditionalAlcoholRepository;

    // 무작위로 3개의 전통주를 조회하는 메서드
    public List<TraditionalAlcohol> findRandom3Alcohols() {
        return traditionalAlcoholRepository.findRandom3Alcohols();
    }

    // 무작위로 2개의 전통주를 조회하는 메서드
    public List<TraditionalAlcohol> findRandom2Alcohols() {
        return traditionalAlcoholRepository.findRandom2Alcohols();
    }

    // 음식 이름으로 어울리는 술을 리뷰 순으로 조회하는 메서드
    public List<TraditionalAlcohol> findAlcoholByReview(String name) {
        return traditionalAlcoholRepository.findByNameOrderByReviewCount(name);
    }

    // 음식 이름으로 어울리는 술을 평점 순으로 조회하는 메서드
    public List<TraditionalAlcohol> findAlcoholByScore(String name) {
        log.info("서비스 값 = {}", name);
        return traditionalAlcoholRepository.findByNameOrderByScore(name);
    }
}
