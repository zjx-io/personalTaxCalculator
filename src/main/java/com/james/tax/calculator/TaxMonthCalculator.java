package com.james.tax.calculator;

import com.alibaba.fastjson.JSON;
import com.james.tax.Calculator;
import com.james.tax.LevelRateBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 月薪个税计算器
 */
public class TaxMonthCalculator implements Calculator {


    private static final List<LevelRateBean> monthLevelRateBeans;

    public static final String CONFIG = "[" +
            "{'levelMoney': 36000, 'rate': '0.03','quickCalMoney':0}" +
            "{'levelMoney': 144000, 'rate': '0.1','quickCalMoney':2520}" +
            "{'levelMoney': 300000, 'rate': '0.2','quickCalMoney':16920}" +
            "{'levelMoney': 420000, 'rate': '0.25','quickCalMoney':31920}" +
            "{'levelMoney': 660000, 'rate': '0.3','quickCalMoney':52920}" +
            "{'levelMoney': 960000, 'rate': '0.35','quickCalMoney':85920}" +
            "{'levelMoney': 1000000000000, 'rate': '0.45','quickCalMoney':181920}" +
            "]";

    static {
        monthLevelRateBeans = JSON.parseArray(CONFIG, LevelRateBean.class);
    }

    public BigDecimal cal(BigDecimal shouldTaxMoney) {
        for (LevelRateBean levelRateBean : monthLevelRateBeans) {
            if (shouldTaxMoney.compareTo(levelRateBean.getLevelMoney()) <= 0) {
                return shouldTaxMoney.multiply(levelRateBean.getRate()).setScale(2, RoundingMode.HALF_UP)
                        .subtract(levelRateBean.getQuickCalMoney());
            }
        }
        throw new RuntimeException("calculate Month tax error,no level match!");
    }
}
