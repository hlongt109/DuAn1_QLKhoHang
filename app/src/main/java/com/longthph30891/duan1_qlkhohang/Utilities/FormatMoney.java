package com.longthph30891.duan1_qlkhohang.Utilities;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatMoney {
    private FormatMoney() {
    }
    public static String formatCurrency(double amount){
        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        return numberFormat.format(amount);
    }
}
