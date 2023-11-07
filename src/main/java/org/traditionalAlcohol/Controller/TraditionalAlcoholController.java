package org.traditionalAlcohol.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.traditionalAlcohol.Domain.TraditionalAlcohol;
import org.traditionalAlcohol.Service.TraditionalAlcoholService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TraditionalAlcoholController {

    private final TraditionalAlcoholService traditionalAlcoholService;

    @GetMapping("/")
    public String home(Model model){
        log.info("home()");

            List<TraditionalAlcohol> list = traditionalAlcoholService.readAlcoholList();
            model.addAttribute("list", list);

            List<TraditionalAlcohol> list_2nd = traditionalAlcoholService.readAlcoholList2nd();
            model.addAttribute("alcohol", list_2nd);

        return "/home";
    }

    @GetMapping("/detail")
    public String detail(Model model){

        List<TraditionalAlcohol> list = traditionalAlcoholService.readAlcoholList();
        model.addAttribute("list", list);

        return"/detail";
    }

    @GetMapping("/result")
    public String result(Model model){

        List<TraditionalAlcohol> list = traditionalAlcoholService.readAlcoholList();
        model.addAttribute("list", list);

        // 추천 시스템


        return "/result";
    }

    @PostMapping("/result")
    public String result(String result, Model model) {

        List<TraditionalAlcohol> list = traditionalAlcoholService.readAlcoholList();
        model.addAttribute("list", list);


        return "/result";
    }


}
