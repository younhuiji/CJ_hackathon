package org.traditionalAlcohol.Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.traditionalAlcohol.Domain.SimAlcohol;
import org.traditionalAlcohol.Domain.TraditionalAlcohol;
import org.traditionalAlcohol.Service.FlaskAPIService;
import org.traditionalAlcohol.Service.SimAlcoholService;
import org.traditionalAlcohol.Service.TraditionalAlcoholService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TraditionalAlcoholController {

    private final TraditionalAlcoholService traditionalAlcoholService;
    private final SimAlcoholService simAlcoholService;
    private final FlaskAPIService flaskAPIService;

    @GetMapping("/")
    public String home(Model model) {
        log.info("home()");

        // TraditionalAlcohol 목록 가져오기
        List<TraditionalAlcohol> list = traditionalAlcoholService.findRandom3Alcohols();
        model.addAttribute("list", list);

        // 또 다른 TraditionalAlcohol 목록 가져오기
        List<TraditionalAlcohol> list2 = traditionalAlcoholService.findRandom2Alcohols();
        model.addAttribute("alcohol", list2);

        return "/home";
    }

    @GetMapping("/detail")
    public String detail(Model model) {
        // TraditionalAlcohol 목록 가져오기
        List<TraditionalAlcohol> list = traditionalAlcoholService.findRandom3Alcohols();
        model.addAttribute("list", list);

        return "/detail";
    }

    @PostMapping("/detail")
    public String uploadButtonClicked(@RequestParam("imgFile") MultipartFile imgFile) {
        try {
            // 이미지 파일을 Flask 서버로 업로드
            String flaskServerUrl = "http://127.0.0.1:5000/processImage";

            // MultipartFile을 직접 추출한 이미지 파일을 Flask 서버로 전송
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // MultipartFile을 FormData에 추가
            ByteArrayResource fileResource = new ByteArrayResource(imgFile.getBytes()) {
                @Override
                public String getFilename() {
                    return imgFile.getOriginalFilename();
                }
            };
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", fileResource);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // RestTemplate을 사용하여 파일을 Flask 서버로 전송
            RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
            String result = restTemplate.postForObject(flaskServerUrl, requestEntity, String.class);

            // 객체 추출
            String resultName = null;
            try {
                JSONArray jsonArray = new JSONArray(result);
                if (jsonArray.length() > 0) {
                    resultName = jsonArray.getString(0); // 첫 번째 요소 추출
                    System.out.println("Result Name: " + resultName);
                } else {
                    resultName = "null";
                    System.out.println("No result found");
                }
            } catch (JSONException e) {
                // JSON 파싱 오류가 발생한 경우 처리
                e.printStackTrace();
                resultName = "null";
            }

            return "redirect:/result?paramName=" + resultName;
        } catch (IOException e) {
            e.printStackTrace();
            return "Image upload and saving failed: " + e.getMessage();
        }
    }

    // RestTemplate의 ClientHttpRequestFactory를 설정
    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(timeout);
        factory.setConnectTimeout(timeout);
        return factory;
    }

    @GetMapping("/result")
    public String result(Model model, @RequestParam("paramName") String paramName) {
        // 파라미터로 전달된 paramName을 모델에 추가
        model.addAttribute("paramName", paramName);

        // TraditionalAlcohol 목록 가져오기
        List<TraditionalAlcohol> list = traditionalAlcoholService.findRandom3Alcohols();
        model.addAttribute("list", list);

        // 음식 이름 번역
        String foodKorea = flaskAPIService.translateWithFlaskAPI(paramName, "en");
        model.addAttribute("foodKorea", foodKorea);
        log.info("번역전={}", paramName);
        log.info("번역후={}", foodKorea);

        // 음식 분류 후 술 추천 - 평점순
        List<TraditionalAlcohol> scoreAlcoholList = traditionalAlcoholService.findAlcoholByScore(paramName);
        model.addAttribute("score", scoreAlcoholList);

        // 음식 분류 후 술의 이름 - 평점순
        List<String> scoreAlcoholNames = new ArrayList<>();
        for (TraditionalAlcohol alcohol : scoreAlcoholList) {
            String alcoholName = alcohol.getName();
            scoreAlcoholNames.add(alcoholName);
        }
        log.info("scoreAlcoholNames ={}", scoreAlcoholNames);

        // 술과 유사한 추천 시스템 - 평점순
        List<SimAlcohol> scoreAlcoholResult = simAlcoholService.findAlcoholByScore(scoreAlcoholNames);
        model.addAttribute("simScore", scoreAlcoholResult);

        // 음식 분류 후 술 추천 - 리뷰순
        List<TraditionalAlcohol> reviewAlcoholList = traditionalAlcoholService.findAlcoholByReview(paramName);
        model.addAttribute("review", reviewAlcoholList);

        // 음식 분류 후 술의 이름 - 리뷰순
        List<String> reviewAlcoholNames = new ArrayList<>();
        for (TraditionalAlcohol alcohol : reviewAlcoholList) {
            String ReviewAlcoholName = alcohol.getName();
            reviewAlcoholNames.add(ReviewAlcoholName);
        }

        // 술과 유사한 추천 시스템 - 리뷰순
        List<SimAlcohol> reviewAlcoholResult = simAlcoholService.findAlcoholByReview(reviewAlcoholNames);
        model.addAttribute("simReview", reviewAlcoholResult);

        return "/result";
    }
}
