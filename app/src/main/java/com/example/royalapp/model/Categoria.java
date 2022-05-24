package com.example.royalapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public final class Categoria implements Parcelable {
    public int idCategoria;
    public String nome;
    public String cor;
    public String icone;

    public Categoria(int idCategoria, String nome, String cor, String icone) {
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.cor = cor;
        this.icone = icone;
    }

    protected Categoria(Parcel in) {
        idCategoria = in.readInt();
        nome = in.readString();
        cor = in.readString();
        icone = in.readString();
    }

    public static final Creator<Categoria> CREATOR = new Creator<Categoria>() {
        @Override
        public Categoria createFromParcel(Parcel in) {
            return new Categoria(in);
        }

        @Override
        public Categoria[] newArray(int size) {
            return new Categoria[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Categoria{");
        sb.append("idCategoria=").append(idCategoria);
        sb.append(", nome=").append(nome);
        sb.append(", cor=").append(cor);
        sb.append(", icone=").append(icone);
        sb.append('}');
        return sb.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idCategoria);
        parcel.writeString(nome);
        parcel.writeString(cor);
        parcel.writeString(icone);
    }
}