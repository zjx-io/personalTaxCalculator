package com.james.tax;

import cn.hutool.core.util.StrUtil;
import com.james.tax.enums.CalTypeEnum;
import com.james.tax.util.TaxUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * run me
 *
 * @date 2020/12/18
 */
@Slf4j
public class TaxCalculateMain {

    public static void main(String[] args) {
        calc();
    }

    public static void calc() {
        //总税汇总
        BigDecimal totalTax = BigDecimal.ZERO;
        //总收入汇总
        BigDecimal totalSalaryMoney = BigDecimal.ZERO;
        //总保险汇总
        BigDecimal totalInsMoney = BigDecimal.ZERO;
        //总公积金汇总
        BigDecimal totalFundMoney = BigDecimal.ZERO;
        //抵扣免税金额汇总
        BigDecimal totalNotTaxMoney = BigDecimal.ZERO;
        //起征金额汇总
        BigDecimal totalStartNotTaxMoney = BigDecimal.ZERO;
        //应税金额汇总
        BigDecimal totalShouldTaxMoney = BigDecimal.ZERO;
        //总应纳税金额
        String monthMoneysStr = TaxPropsProvider.getString("EXT_MONTH_MONEY");
        String[] monthMoneysArray = StrUtil.splitToArray(monthMoneysStr, "|");
        int totalMonth = TaxPropsProvider.getInt("MONTH_COUNT");
        for (int i = 1; i <= totalMonth; i++) {
            //开始i月
            log.info("====第" + i + "月明细====");
            BigDecimal monthMoney = TaxPropsProvider.getBigDecimal("FIXED_MONTH_MONEY");
            if (i <= monthMoneysArray.length) {
                monthMoney = new BigDecimal(monthMoneysArray[i - 1]).add(monthMoney);
            }
            //月薪=固定+可配置化扩展月薪.
            log.info(i + "月薪水为:" + monthMoney);
            //
            BigDecimal insFundBaseMoney = TaxUtil.decideMoney(i, monthMoney);

            BigDecimal insMoney = CalCulatorFactory.getInstance(CalTypeEnum.INSURANCE).cal(insFundBaseMoney);
            BigDecimal fundMoney = CalCulatorFactory.getInstance(CalTypeEnum.FUND).cal(insFundBaseMoney);
            BigDecimal notTaxMoney = TaxUtil.getNotTaxMoney(i);
            BigDecimal startNotTaxMoney = TaxPropsProvider.getBigDecimal("START_NOT_TAX_MONEY");
            //汇总数据
            totalSalaryMoney = totalSalaryMoney.add(monthMoney);
            totalInsMoney = totalInsMoney.add(insMoney);
            totalFundMoney = totalFundMoney.add(fundMoney);
            totalStartNotTaxMoney = totalStartNotTaxMoney.add(startNotTaxMoney);
            totalNotTaxMoney = totalNotTaxMoney.add(notTaxMoney);
            //截至当期总应纳税金额
            BigDecimal shouldTaxMoney = totalSalaryMoney
                    .subtract(totalInsMoney)
                    .subtract(totalFundMoney)
                    .subtract(totalStartNotTaxMoney)
                    .subtract(totalNotTaxMoney);
            log.info("当期累计税前收入:" + totalSalaryMoney);
            log.info("当期累计应纳税收入:" + shouldTaxMoney);
            BigDecimal curTotalTax = CalCulatorFactory.getInstance(CalTypeEnum.MONTH_TAX).cal(shouldTaxMoney);
            BigDecimal tax = curTotalTax.subtract(totalTax);
            log.info("当期累计应纳税:" + curTotalTax);
            log.info("当期累计已纳税:" + totalTax);
            log.info("本期个人所得税:" + tax.toPlainString());
            //汇总税
            totalTax = curTotalTax;
            totalShouldTaxMoney = shouldTaxMoney;
        }

        log.info("=========年终统计================");
        BigDecimal taxYear;
        BigDecimal yearBonusMoney = TaxPropsProvider.getBigDecimal("YEAR_BONUS_MONEY");
        log.info("一次性奖金收入:" + yearBonusMoney);
        totalShouldTaxMoney = totalShouldTaxMoney.add(yearBonusMoney);
        if (TaxPropsProvider.getBoolean("YEAR_TAX_SINGLE")) {
            taxYear = CalCulatorFactory.getInstance(CalTypeEnum.YEAR_TAX).cal(yearBonusMoney);
            log.info("一次性奖金收入纳税:" + taxYear.toPlainString());
        } else {
            BigDecimal curTotalTax = CalCulatorFactory.getInstance(CalTypeEnum.MONTH_TAX).cal(totalShouldTaxMoney);
            taxYear = curTotalTax.subtract(totalTax);
            log.info("当期累计应纳税金额:" + curTotalTax);
            log.info("当期累计已纳税金额:" + totalTax);
            log.info("一次性奖金收入纳税:" + taxYear.toPlainString());
        }
        log.info("=========汇总统计================");
        log.info("====税前收入信息====");
        log.info("全年总收入:" + totalSalaryMoney.add(yearBonusMoney).toPlainString());
        log.info("全年月薪工资收入:" + totalSalaryMoney.toPlainString());
        log.info("一次性奖金收入:" + yearBonusMoney);
        log.info("====纳税信息====");
        log.info("全年总个人所得税:" + totalTax.add(taxYear).toPlainString());
        log.info("全年月薪工资所得税:" + totalTax.toPlainString());
        log.info("一次性奖金收入所得税:" + taxYear.toPlainString());
        log.info("====五险一金信息====");
        log.info("全年五险一金总个人支出:" + totalInsMoney.add(totalFundMoney).toPlainString());
        log.info("全年五险总个人支出:" + totalInsMoney.toPlainString());
        log.info("全年公积金总个人支出:" + totalFundMoney.toPlainString());
        log.info("====税后收入信息====");
        BigDecimal totalRealMonthMoney = totalSalaryMoney.subtract(totalInsMoney).
                subtract(totalFundMoney).subtract(totalTax);
        log.info("全年月工资税后总收入:" + totalRealMonthMoney.toPlainString());
        log.info("全年公积金账户收入:" + totalFundMoney.multiply(new BigDecimal("2")).toPlainString());
        log.info("一次性奖金税后收入:" + yearBonusMoney.subtract(taxYear).
                toPlainString());
        log.info("全年汇总实际收入(税后现金+公积金):" + totalRealMonthMoney.add(yearBonusMoney.subtract(taxYear)).add(totalFundMoney.multiply(new BigDecimal("2"))).toPlainString());
    }


}
