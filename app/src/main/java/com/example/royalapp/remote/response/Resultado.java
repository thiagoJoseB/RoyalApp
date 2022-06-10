package com.example.royalapp.remote.response;

import com.google.gson.annotations.SerializedName;

public class Resultado {
    @SerializedName("status") public int status;
    @SerializedName("found") public boolean found;
    @SerializedName("reset") public boolean reset;
}
