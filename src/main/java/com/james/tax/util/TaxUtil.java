package com.james.tax.util;

import cn.hutool.core.util.StrUtil;
import com.james.tax.TaxPropsProvider;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class TaxUtil {
    public static BigDecimal decideMoney(int month, BigDecimal monthMoney) {

        //支持切换基数
        BigDecimal maxWXYJMoney;
        BigDecimal minWXYJMoney;
        //每年都会调整社保基数,支持配置调整月份和调整前后基数
        if (month < TaxPropsProvider.getInt("WXYJ_CHANGE_MONTH")) {
            maxWXYJMoney = TaxPropsProvider.getBigDecimal("MAX_INSFUND_MONEY_BEFORE");
            minWXYJMoney = TaxPropsProvider.getBigDecimal("MIN_INSFUND_MONEY_BEFORE");
        } else {
            maxWXYJMoney = TaxPropsProvider.getBigDecimal("MAX_INSFUND_MONEY_AFTER");
            minWXYJMoney = TaxPropsProvider.getBigDecimal("MIN_INSFUND_MONEY_AFTER");
        }
        //支持社保缴纳策略可配置化
        BigDecimal shouldWXYJMoney = BigDecimal.ZERO;
        int WXYJ_FLAG = TaxPropsProvider.getInt("WXYJ_FLAG");
        //0:正常模式,自定义缴纳金额
        if (WXYJ_FLAG == 0) {
            shouldWXYJMoney = TaxPropsProvider.getBigDecimal("WXYJ_REAL_MONEY");
        }
        //1:永远最大模式,按照最大模式缴纳
        if (WXYJ_FLAG == 1) {
            shouldWXYJMoney = maxWXYJMoney;
        }
        //-1:永远按照最小缴纳
        if (WXYJ_FLAG == -1) {
            shouldWXYJMoney = minWXYJMoney;
        }
        log.info("五险一金实际缴纳基数为:" + shouldWXYJMoney.toPlainString());
        return shouldWXYJMoney;

    }

    public static BigDecimal getNotTaxMoney(int i) {

        BigDecimal fixedNotTaxMoney = TaxPropsProvider.getBigDecimal("FIXED_NOT_TAX_MONEY");
        BigDecimal result = fixedNotTaxMoney;
        String extNotTaxMoneyStr = TaxPropsProvider.getString("EXT_NOT_TAX_MONEY");
        if (!StrUtil.isBlank(extNotTaxMoneyStr)) {
            String[] extNotTaxMoneyArray = StrUtil.splitToArray(extNotTaxMoneyStr, "|");
            if (i <= extNotTaxMoneyArray.length) {
                result = new BigDecimal(extNotTaxMoneyArray[i - 1]).add(fixedNotTaxMoney);
            }
        }
        return result;
    }
}
