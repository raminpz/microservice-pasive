package com.nttdata.mpasive.utilities.util.impl;

import com.nttdata.mpasive.utilities.util.IAccountSpecificationsUtils;
import org.springframework.stereotype.Component;


@Component
public class AccountSpecificationsUtilsImpl implements IAccountSpecificationsUtils {

    @Override
    public Double roundDouble(Double numberToRound, int decimalPlaces) {
        System.out.println(numberToRound);
        System.out.println(numberToRound);
        System.out.println(numberToRound);
        System.out.println(numberToRound);
        System.out.println(numberToRound);
        System.out.println(decimalPlaces);
        System.out.println(decimalPlaces);
        System.out.println(decimalPlaces);
        System.out.println(decimalPlaces);
        System.out.println(decimalPlaces);
        numberToRound = numberToRound * Math.pow(10, decimalPlaces);
        numberToRound = (double) (Math.round(numberToRound));
        return numberToRound / Math.pow(10, decimalPlaces);
    }

    @Override
    public Double applyInterests(Double amount, Double interestPercentage) {
        return (100 + interestPercentage)/100 * amount;
    }

    @Override
    public Double calculateCommission(Double amount, Double interestPercentage) {
        return interestPercentage/100 * amount;
    }
}
