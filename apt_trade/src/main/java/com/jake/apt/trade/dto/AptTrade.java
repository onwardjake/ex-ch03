package com.jake.apt.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AptTrade {
    private Long id;
    private String lawdCd;
    private String sigungu;
    private String jibun;
    private String jibunBonbun;
    private String jibunBubun;
    private String aptName;
    private Double exclusiveAreaM2;
    private Double exclusiveAreaPyeong;
    private Integer contractYearmonth;
    private Integer contractDay;
    private LocalDate contractDate;
    private Integer dealAmount10k;
    private Long dealAmountKrw;
    private String dong;
    private Integer floor;
    private Integer buildYear;
    private String roadName;
    private String cancelYn;
    private LocalDate cancelDate;
    private String tradeType;
    private String brokerArea;
    private String aptType;
}
