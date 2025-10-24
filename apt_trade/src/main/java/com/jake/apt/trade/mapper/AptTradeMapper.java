package com.jake.apt.trade.mapper;

import com.jake.apt.trade.dto.AptTrade;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AptTradeMapper {
    long countAptTrades(Map<String, Object> params);
    List<AptTrade> selectAptTrades(Map<String, Object> params);
}
