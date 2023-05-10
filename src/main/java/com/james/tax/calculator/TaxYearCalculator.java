package com.james.tax.calculator;

import com.alibaba.fastjson.JSON;
import com.james.tax.Calculator;
import com.james.tax.LevelRateBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 年终奖税率计算器
 */
public class TaxYearCalculator implements Calculator {


    private static final List<LevelRateBean> yearLevelRateBeans;

    public static final String CONFIG = "[" +
            "{'levelMoney': 3000, 'rate': '0.03','quickCalMoney':0}" +
            "{'levelMoney': 12000, 'rate': '0.1','quickCalMoney':210}" +
            "{'levelMoney': 25000, 'rate': '0.2','quickCalMoney':1410}" +
            "{'levelMoney': 35000, 'rate': '0.25','quickCalMoney':2660}" +
            "{'levelMoney': 55000, 'rate': '0.3','quickCalMoney':4410}" +
            "{'levelMoney': 80000, 'rate': '0.35','quickCalMoney':7160}" +
            "{'levelMoney': 1000000000000, 'rate': '0.45','quickCalMoney':15160}" +
            "]";

    static {
        yearLevelRateBeans = JSON.parseArray(CONFIG, LevelRateBean.class);
    }

    public BigDecimal cal(BigDecimal shouldTaxMoney) {
        BigDecimal yearMonth = shouldTaxMoney.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        for (LevelRateBean levelRateBean : yearLevelRateBeans) {
            if (yearMonth.compareTo(levelRateBean.getLevelMoney()) <= 0) {
                return shouldTaxMoney.multiply(levelRateBean.getRate()).setScale(2, RoundingMode.HALF_UP)
                        .subtract(levelRateBean.getQuickCalMoney());
            }
        }
        throw new RuntimeException("calculate year tax error,no level match!");
    }
}
