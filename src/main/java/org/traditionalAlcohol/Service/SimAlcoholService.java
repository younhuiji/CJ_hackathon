package org.traditionalAlcohol.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.traditionalAlcohol.Domain.SimAlcohol;
import org.traditionalAlcohol.Repository.SimAlcoholRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SimAlcoholService {
    private final SimAlcoholRepository simAlcoholRepository;

    // 리뷰순으로 술 정보를 조회하는 메서드
    public List<SimAlcohol> findAlcoholByReview(List<String> reviewAlcoholList) {
        String searchValues = String.join(",", reviewAlcoholList);
        return simAlcoholRepository.findSimilarAlcoholByName(searchValues);
    }

    // 평점순으로 술 정보를 조회하는 메서드
    public List<SimAlcohol> findAlcoholByScore(List<String> scoreAlcoholList) {
        String searchValues = String.join(",", scoreAlcoholList);
        log.info("서비스 request값 = {}", scoreAlcoholList);
        log.info("서비스 결과값 = {}", simAlcoholRepository.findSimilarAlcoholByName(searchValues));
        return simAlcoholRepository.findSimilarAlcoholByName(searchValues);
    }
}
