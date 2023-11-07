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

    public List<TraditionalAlcohol> readAlcoholList() {

        return traditionalAlcoholRepository.findRandom3Alcohols();

    }

    public List<TraditionalAlcohol> readAlcoholList2nd() {
        return traditionalAlcoholRepository.findRandom2Alcohols();
    }
}
