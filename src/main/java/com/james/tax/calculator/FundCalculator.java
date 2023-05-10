package com.james.tax.calculator;

import com.james.tax.Calculator;
import com.james.tax.TaxPropsProvider;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 公积金计算器
 */
@Slf4j
public class FundCalculator implements Calculator {

    public BigDecimal cal(BigDecimal shouldWXYJMoney) {

        BigDecimal gjjMoney = multiply4Fund(shouldWXYJMoney, TaxPropsProvider.getBigDecimal("GJJ_RATE"));
        BigDecimal supGjjMoney = multiply4Fund(shouldWXYJMoney, TaxPropsProvider.getBigDecimal("SUP_GJJ_RATE"));
        log.info("公积金:" + gjjMoney);
        log.info("补充公积金:" + supGjjMoney);
        return gjjMoney.add(supGjjMoney);
    }

    private static BigDecimal multiply4Fund(BigDecimal from, BigDecimal too) {
        return (from.multiply(too).setScale(0, RoundingMode.HALF_UP));
    }
}
