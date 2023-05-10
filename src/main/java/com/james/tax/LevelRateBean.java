package com.james.tax;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 阶梯税率对象
 */
@Getter
@Setter
public class LevelRateBean {
    private BigDecimal levelMoney;
    private BigDecimal rate;
    private BigDecimal quickCalMoney;


}
