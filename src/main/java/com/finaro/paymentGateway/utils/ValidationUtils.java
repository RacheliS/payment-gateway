package com.finaro.paymentGateway.utils;

import com.finaro.paymentGateway.enums.Currency;
import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

@UtilityClass
public class ValidationUtils {

    public static boolean isEmailValid(String email) {
        String emailValidationRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailValidationRegex);
        return pattern.matcher(email).matches();
    }

    public boolean isPositiveNumber(Integer number) {
        return Integer.signum(number) > 0;
    }

    public boolean isCreditCardValid(String str) {
        return isLuhnAlgorithmValid(str);
    }

    public boolean isLuhnAlgorithmValid(String cardNo) {
        int nDigits = cardNo.length();
        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {
            int d = cardNo.charAt(i) - '0';
            if (isSecond)
                d = d * 2;
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    public boolean isExpiryDateValid(String expiry) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMyy");
            simpleDateFormat.setLenient(false);
            Date expiryDate = simpleDateFormat.parse(expiry);
            return expiryDate.after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isSupportedCurrency(String currencyToVerify) {
        for (Currency curr : Currency.values()) {
            if (curr.name().equalsIgnoreCase(currencyToVerify)) return true;
        }
        return false;
    }
}
