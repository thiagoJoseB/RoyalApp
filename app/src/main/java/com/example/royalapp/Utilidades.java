package com.example.royalapp;

import com.google.gson.Gson;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilidades {
    public static final Locale BRASIL = new Locale("pt", "BR");
    public static final DateFormat FORMATADOR_DIA = DateFormat.getDateInstance(DateFormat.SHORT, BRASIL);
    public static final DateFormat FORMATADOR_DIA_LONGO = DateFormat.getDateInstance(DateFormat.LONG, BRASIL);
    public static final NumberFormat FORMATADOR_MOEDA = NumberFormat.getCurrencyInstance(BRASIL);
    public static final Calendar CALENDARIO = Calendar.getInstance();
    public static final Gson GSON = new Gson();
    public static final Pattern PATTERN_DINHEIRO_BR = Pattern.compile("[R$,.\\s]");

    static {
        FORMATADOR_MOEDA.setRoundingMode(RoundingMode.DOWN);
    }
}
