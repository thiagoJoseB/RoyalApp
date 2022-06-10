package com.example.royalapp;

import static com.example.royalapp.Utilidades.CALENDARIO;

import android.graphics.Bitmap;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Variaveis {

    public static int mesAlvoInicio0 = CALENDARIO.get(Calendar.MONTH);
    public static int anoAlvo = CALENDARIO.get(Calendar.YEAR);

    public static final Map<Integer, Bitmap> IMAGENS = new HashMap<>();
}
