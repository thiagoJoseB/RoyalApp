package com.example.royalapp.remote.response;

import com.example.royalapp.model.Categoria;

import java.math.BigDecimal;
import java.util.List;

public class DashboardData {
    public BigDecimal saldo;
    public BigDecimal receita;
    public BigDecimal despesa;
    public DashboardData.Categorias categorias;

    public static class Categorias{
        public List<Categoria> receitas;
        public List<Categoria> despesas;
    }
}


/*
    {
    "categorias": {
        "despesas": [
            {
                "idCategoria": 2,
                "nome": "Viagens",
                "icone": "flight",
                "cor": "000088"
            },
            {
                "idCategoria": 3,
                "nome": "Alimentação",
                "icone": "restaurant_menu",
                "cor": "008800"
            }
        ],
        "receitas": [
            {
                "idCategoria": 1,
                "nome": "Salário",
                "icone": "attach_money",
                "cor": "880000"
            }
        ]
    },
    "saldo": 0.00,
    "receita": 0.00,
    "despesa": 0.00
}
 */