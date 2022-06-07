package com.example.royalapp;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Extra {
    @SuppressWarnings("unchecked")
    public static Future<Void> rodar(Rodavel a){
        return (Future<Void>) Executors.newSingleThreadExecutor().submit(() -> {
            try {
                a.aceitar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FunctionalInterface
    public static interface Rodavel{
        public void aceitar() throws IOException;
    }
}


