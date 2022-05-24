package com.example.royalapp;

import com.google.gson.Gson;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utilidades {
    public static final Locale BRASIL = new Locale("pt", "BR");
    public static final DateFormat FORMATADOR_DIA = DateFormat.getDateInstance(DateFormat.SHORT, BRASIL);
    public static final NumberFormat FORMATADOR_MOEDA = NumberFormat.getCurrencyInstance(BRASIL);
    public static final Calendar CALENDARIO = Calendar.getInstance();
    public static final Gson GSON = new Gson();

    static {
        FORMATADOR_MOEDA.setRoundingMode(RoundingMode.DOWN);
    }
}
