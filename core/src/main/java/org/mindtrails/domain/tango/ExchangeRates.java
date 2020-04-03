package org.mindtrails.domain.tango;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Data
public class ExchangeRates {
    List<ExchangeRate> exchangeRates;
    public static final String US_DOLLARS = "USD";


    public double convertFromUSDollars(double amount, String currency) {
        if(currency.equals(US_DOLLARS)) {
            return amount;
        }
        for (ExchangeRate rate : exchangeRates) {
            if (rate.rewardCurrency.equals(currency) && rate.baseCurrency.equals(US_DOLLARS)) {
                return round(amount * rate.baseFx, 2);
            }
        }
        throw new RuntimeException("Currency not found:" + currency);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public boolean populated() {
        return this.exchangeRates.size() > 0;
    }

    public Date lastModifedDate() {
        return this.exchangeRates.get(0).lastModifiedDate;
    }
}

@Data
class ExchangeRate {
    Date lastModifiedDate;
    String rewardCurrency;
    String baseCurrency;
    double baseFx;
}