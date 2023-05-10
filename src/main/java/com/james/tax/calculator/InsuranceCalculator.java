package com.james.tax.calculator;

import com.james.tax.Calculator;
import com.james.tax.TaxPropsProvider;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 保险计算器
 */
@Slf4j
public class InsuranceCalculator implements Calculator {

    public BigDecimal cal(BigDecimal monthMoney) {
        BigDecimal sbMoney = multiply4Insurance(monthMoney, TaxPropsProvider.getBigDecimal("SB_RATE"));
        log.info("社保:" + sbMoney);
        BigDecimal ylMoney = multiply4Insurance(monthMoney, TaxPropsProvider.getBigDecimal("YL_RATE"));
        log.info("医疗:" + ylMoney);
        BigDecimal syMoney = multiply4Insurance(monthMoney, TaxPropsProvider.getBigDecimal("SY_RATE"));
        log.info("失业:" + syMoney);
        return sbMoney.add(ylMoney).add(syMoney);
    }

    private static BigDecimal multiply4Insurance(BigDecimal from, BigDecimal too) {
        return (from.multiply(too).setScale(2, RoundingMode.HALF_UP)).setScale(1, RoundingMode.UP);
    }
}
