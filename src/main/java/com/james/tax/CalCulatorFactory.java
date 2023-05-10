package com.james.tax;

import com.james.tax.calculator.FundCalculator;
import com.james.tax.calculator.InsuranceCalculator;
import com.james.tax.calculator.TaxMonthCalculator;
import com.james.tax.calculator.TaxYearCalculator;
import com.james.tax.enums.CalTypeEnum;

public class CalCulatorFactory {

    public static Calculator getInstance(CalTypeEnum calTypeEnum) {
        switch (calTypeEnum) {
            case FUND:
                return new FundCalculator();
            case INSURANCE:
                return new InsuranceCalculator();
            case MONTH_TAX:
                return new TaxMonthCalculator();
            case YEAR_TAX:
                return new TaxYearCalculator();
        }
        throw new RuntimeException("no calculator get");
    }
}
