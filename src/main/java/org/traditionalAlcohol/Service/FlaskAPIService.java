package org.traditionalAlcohol.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.traditionalAlcohol.Dto.TranslationRequest;

@Service
public class FlaskAPIService {
    private final String FLASK_API_URL = "http://127.0.0.1:5000";  // Flask API의 URL
    private final RestTemplate restTemplate;

    public FlaskAPIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String translateWithFlaskAPI(String text, String targetLang) {
        // Flask API의 번역 엔드포인트에 POST 요청을 보냄
        TranslationRequest request = new TranslationRequest(text, targetLang);
        return restTemplate.postForObject(FLASK_API_URL + "/translate", request, String.class);
    }
}
