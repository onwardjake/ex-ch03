package com.jake.apt.trade.controller;

import com.jake.apt.trade.dto.AptTrade;
import com.jake.apt.trade.service.AptTradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/list")
@RequiredArgsConstructor
public class AptTradeController {
    private final AptTradeService aptTradeService;

    @GetMapping
    public String list(
            @RequestParam(required = false) String sigungu,
            @RequestParam(required = false, name = "aptName") String aptName,
            @RequestParam(required = false) Double exclusiveAreaM2,
            @RequestParam(required = false) Integer contractYearMonth,
            @RequestParam(required = false) LocalDate contractDate,
            @RequestParam(required = false) Integer buildYear,

            // 페이지네이션 기본값
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "contractDate") String sort,
            @RequestParam(defaultValue = "desc") String dir,

            Model model
    ) {

        int offset = page * size;

        // 검색 조건 파라미터 Map 생성
        var params = new java.util.HashMap<String, Object>();
        params.put("sigungu", sigungu);
        params.put("aptName", aptName);
        params.put("exclusiveAreaM2", exclusiveAreaM2);
        params.put("contractYearMonth", contractYearMonth);
        params.put("contractDate", contractDate);
        params.put("buildYear", buildYear);
        params.put("limit", size);
        params.put("offset", offset);
        params.put("sort", sort);
        params.put("dir", dir);

        // 조회
        long total = aptTradeService.countAptTrades(params);
        List<AptTrade> items = aptTradeService.selectAptTrades(params);

        // Thymeleaf 모델 전달
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", (int) Math.ceil((double) total / size));
        model.addAttribute("param", params);

        return "apt_trade/list_apt_trade";
    }
}
