package com.jake.apt.trade.service;

import com.jake.apt.trade.dto.AptTrade;
import com.jake.apt.trade.mapper.AptTradeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AptTradeService {
    private final AptTradeMapper aptTradeMapper;

    public List<AptTrade> selectAptTrades(Map<String, Object> params) {
        return aptTradeMapper.selectAptTrades(params);
    }

    public long countAptTrades(HashMap<String, Object> params) {
        return aptTradeMapper.countAptTrades(params);
    }
}
